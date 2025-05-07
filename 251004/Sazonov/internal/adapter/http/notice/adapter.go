package notice

import (
	"resty.dev/v3"
)

type Adapter struct {
	cli *resty.Client
}

func New(cli *resty.Client) *Adapter {
	return &Adapter{
		cli: cli,
	}
}
