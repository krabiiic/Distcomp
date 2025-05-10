from django.urls import path
from .views import CreatorListView, CreatorDetailView, NewsListView, NewsDetailView, MarkListView, MarkDetailView

urlpatterns = [
    # Creator
    path("creators", CreatorListView.as_view(), name="creator-list"),
    path("creators/<int:id>", CreatorDetailView.as_view(), name="creator-detail"),

    # News
    path("news", NewsListView.as_view(), name="news-list"),
    path("news/<int:id>", NewsDetailView.as_view(), name="news-detail"),

    # Mark
    path("marks", MarkListView.as_view(), name="mark-list"),
    path("marks/<int:id>", MarkDetailView.as_view(), name="mark-detail"),
]