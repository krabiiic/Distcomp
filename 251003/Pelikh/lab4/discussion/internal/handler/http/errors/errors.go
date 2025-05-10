package errors

import (
	"net/http"

	"github.com/gin-gonic/gin"
	"github.com/stackus/errors"
)

type HTTPError struct {
	Notice string `json:"Notice,omitempty"`
}

func Error(c *gin.Context, err error) {
	switch {
	case errors.Is(err, errors.ErrNotFound):
		c.JSON(http.StatusNotFound, HTTPError{Notice: err.Error()})

	case errors.Is(err, errors.ErrAlreadyExists):
		c.JSON(http.StatusConflict, HTTPError{Notice: err.Error()})

	case errors.Is(err, errors.ErrBadRequest):
		c.JSON(http.StatusBadRequest, HTTPError{Notice: err.Error()})

	case errors.Is(err, errors.ErrForbidden):
		c.JSON(http.StatusForbidden, HTTPError{Notice: err.Error()})

	case errors.Is(err, errors.ErrAlreadyExists):
		c.JSON(http.StatusConflict, HTTPError{Notice: err.Error()})

	case errors.Is(err, errors.ErrInternalServerError):
		c.JSON(http.StatusInternalServerError, HTTPError{Notice: err.Error()})

	default:
		c.JSON(http.StatusInternalServerError, HTTPError{Notice: err.Error()})
	}
}
