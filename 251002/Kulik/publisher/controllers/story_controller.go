package controllers

import (
	// "context"
	"distributedcomputing/model"
	"distributedcomputing/service"
	"errors"
	"fmt"
	"github.com/go-redis/redis/v8"
	"github.com/labstack/echo/v4"
	"net/http"
	"strconv"
	"encoding/json"
)


type StoryController struct {
	service     *service.StoryService
	redisClient *redis.Client
}

func NewStoryController(service *service.StoryService, redisClient *redis.Client) *StoryController {
	return &StoryController{service: service, redisClient: redisClient}
}

func (sc *StoryController) Create(c echo.Context) error {
	var dto model.StoryRequestTo
	if err := c.Bind(&dto); err != nil {
		return c.JSON(http.StatusBadRequest, WrapErr(err))
	}

	if err := validateStory(dto); err != nil {
		return c.JSON(http.StatusBadRequest, WrapErr(err))
	}

	entity, err := sc.service.Create(dto)
	if err != nil {
		return c.JSON(http.StatusForbidden, WrapErr(err))
	}

	entityJSON, _ := json.Marshal(entity)
	sc.redisClient.Set(ctx, fmt.Sprintf("story:%d", entity.Id), entityJSON, 0)

	return c.JSON(http.StatusCreated, entity)
}

func (sc *StoryController) Get(c echo.Context) error {
	idStr := c.Param("id")
	id, err := strconv.ParseInt(idStr, 10, 64)
	if err != nil {
		return c.JSON(http.StatusBadRequest, "Invalid ID format")
	}

	storyData, err := sc.redisClient.Get(ctx, fmt.Sprintf("story:%d", id)).Result()
	if err == redis.Nil {
		story, err := sc.service.Get(id)
		if err != nil {
			return c.JSON(http.StatusNotFound, WrapErr(err))
		}

		storyJSON, _ := json.Marshal(story)
		sc.redisClient.Set(ctx, fmt.Sprintf("story:%d", id), storyJSON, 0)

		return c.JSON(http.StatusOK, story)
	} else if err != nil {
		return c.JSON(http.StatusInternalServerError, WrapErr(err))
	}

	var story model.StoryResponseTo
	if err := json.Unmarshal([]byte(storyData), &story); err != nil {
		return c.JSON(http.StatusInternalServerError, WrapErr(err))
	}

	return c.JSON(http.StatusOK, story)
}

func (sc *StoryController) GetAll(c echo.Context) error {
	stories, err := sc.service.GetAll()
	if err != nil {
		return c.JSON(http.StatusNotFound, WrapErr(err))
	}
	return c.JSON(http.StatusOK, stories)
}

func (sc *StoryController) Update(c echo.Context) error {
	var dto model.StoryRequestTo
	if err := c.Bind(&dto); err != nil {
		return c.JSON(http.StatusBadRequest, WrapErr(err))
	}

	if err := validateStory(dto); err != nil {
		return c.JSON(http.StatusBadRequest, WrapErr(err))
	}

	if err := sc.service.Update(dto); err != nil {
		return c.JSON(http.StatusInternalServerError, WrapErr(err))
	}

	storyJSON, _ := json.Marshal(dto)
	sc.redisClient.Set(ctx, fmt.Sprintf("story:%d", dto.Id), storyJSON, 0)

	return c.JSON(http.StatusOK, dto)
}

func (sc *StoryController) Delete(c echo.Context) error {
	idStr := c.Param("id")
	id, err := strconv.ParseInt(idStr, 10, 64)
	if err != nil {
		return c.NoContent(http.StatusNoContent)
	}

	if err := sc.service.Delete(id); err != nil {
		return c.NoContent(http.StatusNotFound)
	}

	sc.redisClient.Del(ctx, fmt.Sprintf("story:%d", id))

	return c.NoContent(http.StatusNoContent)
}

func validateStory(dto model.StoryRequestTo) error {
	if len(dto.Title) < 2 || len(dto.Title) > 64 {
		return errors.New("title must be between 2 and 64 characters")
	}
	if len(dto.Content) < 4 || len(dto.Content) > 2048 {
		return errors.New("content must be between 4 and 2048 characters")
	}
	return nil
}
