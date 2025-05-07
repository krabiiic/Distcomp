package adapter

import (
	"fmt"

	httpnotice "github.com/Khmelov/Distcomp/251004/Sazonov/internal/adapter/http/notice"
	kafkanotice "github.com/Khmelov/Distcomp/251004/Sazonov/internal/adapter/kafka/notice"
	"github.com/Khmelov/Distcomp/251004/Sazonov/pkg/kafka"
	"resty.dev/v3"
)

type adapter struct {
	cli      *resty.Client
	producer *kafka.Producer

	asyncNotice *kafkanotice.Adapter
	syncNotice  *httpnotice.Adapter
}

func New(noticeAddr string, producerCfg kafka.ProducerConfig) Adapter {
	cli := resty.New().SetBaseURL(fmt.Sprintf("http://%s/api", noticeAddr)).SetTrace(true)
	producer := kafka.NewProducer(producerCfg)

	return &adapter{
		cli:      cli,
		producer: producer,

		syncNotice:  httpnotice.New(cli),
		asyncNotice: kafkanotice.New(producer),
	}
}

func (a *adapter) SyncNotice() SyncNoticeAdapter {
	return a.syncNotice
}

func (a *adapter) AsyncNotice() AsyncNoticeAdapter {
	return a.asyncNotice
}

func (a *adapter) Close() {
	a.cli.Close()
	a.producer.Close()
}
