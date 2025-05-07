package controllers

import (
	// "context"
	"fmt"
	"github.com/labstack/echo/v4"
	"distributedcomputing/model"
	"distributedcomputing/service"
	"github.com/go-redis/redis/v8"
	"net/http"
	"strconv"
	"errors"
	"encoding/json"
)

type MarkController struct {
	service     *service.MarkService
	redisClient *redis.Client
}

func NewMarkController(service *service.MarkService, redisClient *redis.Client) *MarkController {
	return &MarkController{service: service, redisClient: redisClient}
}

func (mc *MarkController) Create(c echo.Context) error {
	var dto model.MarkRequestTo
	if err := c.Bind(&dto); err != nil {
		return c.JSON(http.StatusBadRequest, WrapErr(err))
	}

	if err := validateMark(dto); err != nil {
		return c.JSON(http.StatusBadRequest, WrapErr(err))
	}

	mark, err := mc.service.Create(dto)
	if err != nil {
		return c.JSON(http.StatusForbidden, WrapErr(err))
	}

	markJSON, _ := json.Marshal(mark)
	mc.redisClient.Set(ctx, fmt.Sprintf("mark:%d", mark.Id), markJSON, 0)

	return c.JSON(http.StatusCreated, mark)
}

func (mc *MarkController) Get(c echo.Context) error {
	idStr := c.Param("id")
	id, err := strconv.ParseInt(idStr, 10, 64)
	if err != nil {
		return c.JSON(http.StatusBadRequest, "Invalid ID format")
	}

	markData, err := mc.redisClient.Get(ctx, fmt.Sprintf("mark:%d", id)).Result()
	if err == redis.Nil {

		mark, err := mc.service.Get(id)
		if err != nil {
			return c.JSON(http.StatusNotFound, WrapErr(err))
		}

		markJSON, _ := json.Marshal(mark)
		mc.redisClient.Set(ctx, fmt.Sprintf("mark:%d", id), markJSON, 0)

		return c.JSON(http.StatusOK, mark)
	} else if err != nil {
		return c.JSON(http.StatusInternalServerError, WrapErr(err))
	}

	var mark model.MarkResponseTo
	if err := json.Unmarshal([]byte(markData), &mark); err != nil {
		return c.JSON(http.StatusInternalServerError, WrapErr(err))
	}

	return c.JSON(http.StatusOK, mark)
}

func (mc *MarkController) GetAll(c echo.Context) error {
	marks, err := mc.service.GetAll()
	if err != nil {
		return c.JSON(http.StatusNotFound, WrapErr(err))
	}
	return c.JSON(http.StatusOK, marks)
}

func (mc *MarkController) Update(c echo.Context) error {
	var dto model.MarkRequestTo
	if err := c.Bind(&dto); err != nil {
		return c.JSON(http.StatusBadRequest, WrapErr(err))
	}

	if err := validateMark(dto); err != nil {
		return c.JSON(http.StatusBadRequest, WrapErr(err))
	}

	if err := mc.service.Update(dto); err != nil {
		return c.JSON(http.StatusForbidden, WrapErr(err))
	}

	markJSON, _ := json.Marshal(dto)
	mc.redisClient.Set(ctx, fmt.Sprintf("mark:%d", dto.Id), markJSON, 0)

	return c.JSON(http.StatusOK, dto)
}

func (mc *MarkController) Delete(c echo.Context) error {
	idStr := c.Param("id")
	id, err := strconv.ParseInt(idStr, 10, 64)
	if err != nil {
		return c.NoContent(http.StatusNoContent)
	}


	if err := mc.service.Delete(id); err != nil {
		return c.NoContent(http.StatusNotFound)
	}

	mc.redisClient.Del(ctx, fmt.Sprintf("mark:%d", id))

	return c.NoContent(http.StatusNoContent)
}

func validateMark(dto model.MarkRequestTo) error {
	if len(dto.Name) < 2 || len(dto.Name) > 32 {
		return errors.New("mark name must be between 2 and 32 characters")
	}
	return nil
}
