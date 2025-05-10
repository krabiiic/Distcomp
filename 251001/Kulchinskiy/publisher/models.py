from enum import unique

from django.db.models import Model, CharField, DateTimeField, ForeignKey, CASCADE, ManyToManyField
from django.core.validators import MinLengthValidator


class Creator(Model):
    login = CharField(max_length=64, validators=[MinLengthValidator(2)], unique=True)
    password = CharField(max_length=128, validators=[MinLengthValidator(8)])
    firstname = CharField(max_length=64, validators=[MinLengthValidator(2)])
    lastname = CharField(max_length=64, validators=[MinLengthValidator(2)])

    class Meta:
        db_table = 'tbl_creator'


class Mark(Model):
    name = CharField(max_length=32, validators=[MinLengthValidator(2)], unique=True)

    class Meta:
        db_table = 'tbl_mark'


class News(Model):
    creator = ForeignKey(to=Creator, related_name='news', on_delete=CASCADE)
    title = CharField(max_length=64, validators=[MinLengthValidator(2)], unique=True)
    content = CharField(max_length=2048, validators=[MinLengthValidator(4)])
    created = DateTimeField(auto_now_add=True)
    modified = DateTimeField(auto_now=True)
    # marks = ManyToManyField(to=Mark, null=True)

    class Meta:
        db_table = 'tbl_news'
