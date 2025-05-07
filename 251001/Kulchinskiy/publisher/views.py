from drf_spectacular.utils import extend_schema
from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework import status
from rest_framework.permissions import AllowAny
from rest_framework.generics import get_object_or_404
from publisher.models import Creator, News, Mark
from publisher.serializers import CreatorSerializer, NewsSerializer, MarkSerializer


class CreatorListView(APIView):
    permission_classes = [AllowAny]

    @extend_schema(
        methods=["GET"],
        responses={200: CreatorSerializer(many=True)},
    )
    def get(self, request):
        creators = Creator.objects.all()
        serializer = CreatorSerializer(creators, many=True)
        return Response(serializer.data)

    @extend_schema(
        methods=["POST"],
        request=CreatorSerializer,
        responses={201: CreatorSerializer},
    )
    def post(self, request):
        serializer = CreatorSerializer(data=request.data)
        if serializer.is_valid():
            creator = serializer.save()
            return Response(serializer.data, status=status.HTTP_201_CREATED)
        return Response(serializer.errors, status=status.HTTP_403_FORBIDDEN)

    @extend_schema(
        methods=["PUT"],
        request=CreatorSerializer,
        responses={200: CreatorSerializer},
    )
    def put(self, request):
        if 'id' in request.data:
            creator = get_object_or_404(Creator, id=request.data['id'])
            serializer = CreatorSerializer(creator, data=request.data, partial=True)
            if serializer.is_valid():
                serializer.save()
                return Response(serializer.data)
            return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

        return Response(data={'error': 'id not provided'}, status=status.HTTP_400_BAD_REQUEST)


class CreatorDetailView(APIView):
    permission_classes = [AllowAny]

    @extend_schema(
        methods=["GET"],
        responses={200: CreatorSerializer},
    )
    def get(self, request, id):
        creator = get_object_or_404(Creator, id=id)
        serializer = CreatorSerializer(creator)
        return Response(serializer.data)

    @extend_schema(
        methods=["DELETE"],
        responses={204: None},
    )
    def delete(self, request, id):
        creator = get_object_or_404(Creator, id=id)
        creator.delete()
        return Response(status=status.HTTP_204_NO_CONTENT)


class NewsListView(APIView):
    permission_classes = [AllowAny]

    @extend_schema(
        methods=["GET"],
        responses={200: NewsSerializer(many=True)},
    )
    def get(self, request):
        news = News.objects.all()
        serializer = NewsSerializer(news, many=True)
        return Response(serializer.data)

    @extend_schema(
        methods=["POST"],
        request=NewsSerializer,
        responses={201: NewsSerializer},
    )
    def post(self, request):
        serializer = NewsSerializer(data=request.data)
        if serializer.is_valid():
            news_item = serializer.save()
            return Response(serializer.data, status=status.HTTP_201_CREATED)
        return Response(serializer.errors, status=status.HTTP_403_FORBIDDEN)

    @extend_schema(
        methods=["PUT"],
        request=CreatorSerializer,
        responses={200: CreatorSerializer},
    )
    def put(self, request):
        if 'id' in request.data:
            news = get_object_or_404(News, id=request.data['id'])
            serializer = NewsSerializer(news, data=request.data, partial=True)
            if serializer.is_valid():
                serializer.save()
                return Response(serializer.data)
            return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

        return Response(data={'error': 'id not provided'}, status=status.HTTP_400_BAD_REQUEST)


class NewsDetailView(APIView):
    permission_classes = [AllowAny]

    @extend_schema(
        methods=["GET"],
        responses={200: NewsSerializer},
    )
    def get(self, request, id):
        news_item = get_object_or_404(News, id=id)
        serializer = NewsSerializer(news_item)
        return Response(serializer.data)

    @extend_schema(
        methods=["DELETE"],
        responses={204: None},
    )
    def delete(self, request, id):
        news_item = get_object_or_404(News, id=id)
        news_item.delete()
        return Response(status=status.HTTP_204_NO_CONTENT)


class MarkListView(APIView):
    permission_classes = [AllowAny]

    @extend_schema(
        methods=["GET"],
        responses={200: MarkSerializer(many=True)},
    )
    def get(self, request):
        marks = Mark.objects.all()
        serializer = MarkSerializer(marks, many=True)
        return Response(serializer.data)

    @extend_schema(
        methods=["POST"],
        request=MarkSerializer,
        responses={201: MarkSerializer},
    )
    def post(self, request):
        serializer = MarkSerializer(data=request.data)
        if serializer.is_valid():
            mark = serializer.save()
            return Response(serializer.data, status=status.HTTP_201_CREATED)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

    @extend_schema(
        methods=["PUT"],
        request=CreatorSerializer,
        responses={200: CreatorSerializer},
    )
    def put(self, request):
        if 'id' in request.data:
            mark = get_object_or_404(Mark, id=request.data['id'])
            serializer = MarkSerializer(mark, data=request.data, partial=True)
            if serializer.is_valid():
                serializer.save()
                return Response(serializer.data)
            return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

        return Response(data={'error': 'id not provided'}, status=status.HTTP_400_BAD_REQUEST)


class MarkDetailView(APIView):
    permission_classes = [AllowAny]

    @extend_schema(
        methods=["GET"],
        responses={200: MarkSerializer},
    )
    def get(self, request, id):
        mark = get_object_or_404(Mark, id=id)
        serializer = MarkSerializer(mark)
        return Response(serializer.data)

    @extend_schema(
        methods=["DELETE"],
        responses={204: None},
    )
    def delete(self, request, id):
        mark = get_object_or_404(Mark, id=id)
        mark.delete()
        return Response(status=status.HTTP_204_NO_CONTENT)
