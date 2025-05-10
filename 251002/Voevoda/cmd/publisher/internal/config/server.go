package config

type Server struct {
	Address             string `mapstructure:"address" validate:"required"`
	NoteRedirectBaseURL string `mapstructure:"note_redirect_base_url" validate:"required"`
}
