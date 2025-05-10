from rest_framework.routers import DefaultRouter


class OptionalSlashRouter(DefaultRouter):
    """Кастомный роутер с необязательным слешем на конце урла"""
    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)
        self.trailing_slash = '/?'