from rest_framework.serializers import ModelSerializer

from publisher.models import Creator, News, Mark


class CreatorSerializer(ModelSerializer):
    class Meta:
        model = Creator
        fields = '__all__'


class NewsSerializer(ModelSerializer):
    class Meta:
        model = News
        fields = '__all__'

    def to_internal_value(self, data):
        if 'creatorId' in data:
            data['creator'] = data.pop('creatorId')

        if 'marks' in data:
            marks_names = data.pop('marks')
            marks_ids = []
            for name in marks_names:
                Mark.objects.create(name=name)
            data['marks'] = marks_ids

        return super().to_internal_value(data)

    def to_representation(self, instance):
        data = super().to_representation(instance)
        if 'creator' in data:
            data['creatorId'] = data.pop('creator')
        return data


class MarkSerializer(ModelSerializer):
    class Meta:
        model = Mark
        fields = '__all__'
