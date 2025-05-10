from django.core.cache import cache
from django.shortcuts import get_object_or_404
from rest_framework.permissions import AllowAny
from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework import status
from drf_spectacular.utils import extend_schema

from discussion.models import Comment
from discussion.serializers import CommentSerializer
from publisher.serializers import CreatorSerializer

def get_request_port(request):
    return request.META.get('SERVER_PORT')

class CommentListView(APIView):
    permission_classes = [AllowAny]

    def is_redis_mode(self, request):
        print(get_request_port(request))
        return get_request_port(request) == "8001"

    @extend_schema(methods=["GET"], responses={200: CommentSerializer(many=True)})
    def get(self, request):
        if self.is_redis_mode(request):
            cached = cache.get("comments_all")
            if cached:
                return Response(cached)

            comments = Comment.objects.all()
            serializer = CommentSerializer(comments, many=True)
            cache.set("comments_all", serializer.data, timeout=60)
            return Response(serializer.data)

        # Fallback for standard backend
        comments = Comment.objects.all()
        serializer = CommentSerializer(comments, many=True)
        return Response(serializer.data)

    @extend_schema(methods=["POST"], request=CommentSerializer, responses={201: CommentSerializer})
    def post(self, request):
        serializer = CommentSerializer(data=request.data)

        if serializer.is_valid():
            comment = serializer.save()
            if self.is_redis_mode(request):
                cache.delete("comments_all")

            return Response(serializer.data, status=status.HTTP_201_CREATED)

        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

    @extend_schema(methods=["PUT"], request=CreatorSerializer, responses={200: CreatorSerializer})
    def put(self, request):
        if 'id' not in request.data:
            return Response(data={'error': 'id not provided'}, status=status.HTTP_400_BAD_REQUEST)

        comment = get_object_or_404(Comment, id=request.data['id'])
        serializer = CommentSerializer(comment, data=request.data, partial=True)

        if serializer.is_valid():
            serializer.save()

            if self.is_redis_mode(request):
                # Invalidate both specific comment and full list
                cache.delete("comments_all")
                cache.delete(f"comment_{request.data['id']}")

            return Response(serializer.data)

        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

class CommentDetailView(APIView):
    permission_classes = [AllowAny]

    def is_redis_mode(self, request):
        return get_request_port(request) == "8001"

    @extend_schema(methods=["GET"], responses={200: CommentSerializer})
    def get(self, request, id):
        cache_key = f"comment_{id}"
        if self.is_redis_mode(request):
            cached = cache.get(cache_key)
            if cached:
                return Response(cached)

        comment = get_object_or_404(Comment, id=id)
        serializer = CommentSerializer(comment)

        if self.is_redis_mode(request):
            cache.set(cache_key, serializer.data, timeout=120)

        return Response(serializer.data)

    @extend_schema(methods=["DELETE"], responses={204: None})
    def delete(self, request, id):
        comment = get_object_or_404(Comment, id=id)
        comment.delete()
        if self.is_redis_mode(request):
            cache.delete("comments_all")
            cache.delete(f"comment_{id}")
        return Response(status=status.HTTP_204_NO_CONTENT)

