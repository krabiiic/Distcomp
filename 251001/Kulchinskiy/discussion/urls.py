from django.urls import path

from discussion.views import CommentListView, CommentDetailView

urlpatterns = [
    path("comments", CommentListView.as_view(), name="comment-list"),
    path("comments/<int:id>", CommentDetailView.as_view(), name="comment-detail"),
]