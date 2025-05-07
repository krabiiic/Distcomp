from rest_framework import serializers

class CommentSerializer(serializers.Serializer):
    id = serializers.IntegerField(read_only=True)
    content = serializers.CharField(min_length=2, max_length=2048)
    newsId = serializers.IntegerField()

    def create(self, validated_data):
        from .models import Comment
        from .helpers import get_next_id

        validated_data['id'] = get_next_id('comment')
        return Comment.create(**validated_data)

    def update(self, instance, validated_data):
        if 'id' in validated_data:
            instance.id = validated_data['id']

        if 'content' in validated_data:
            instance.content = validated_data['content']

        if 'newsId' in validated_data:
            instance.newsId = validated_data['newsId']

        instance.save()

        return instance
