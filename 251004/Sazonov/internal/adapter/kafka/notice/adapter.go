package notice

import kafkapkg "github.com/Khmelov/Distcomp/251004/Sazonov/pkg/kafka"

type Adapter struct {
	producer *kafkapkg.Producer
}

func New(producer *kafkapkg.Producer) *Adapter {
	return &Adapter{
		producer: producer,
	}
}
