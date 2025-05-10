package mapper

import (
	noticeModel "lab3/publisher/internal/model"
)

func MapHTTPNoticeToModel(notice noticeModel.Notice) noticeModel.Notice {
	return noticeModel.Notice{
		ID:      notice.ID,
		TopicID: notice.TopicID,
		Content: notice.Content, // Changed from Content
	}
}

func MapModelToHTTPNotice(notice noticeModel.Notice) noticeModel.Notice {
	return noticeModel.Notice{
		ID:      notice.ID,
		TopicID: notice.TopicID,
		Content: notice.Content, // Changed from Content
	}
}
