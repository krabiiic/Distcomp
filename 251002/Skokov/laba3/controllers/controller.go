package controllers

import (
	"encoding/json"
	"fmt"
	"io"
	"net/http"

	"github.com/gofiber/fiber/v2"
)

func GetMessageByID(c *fiber.Ctx) error {
	target_url := "http://laba2:24110" + c.OriginalURL()
	fmt.Printf("target_url: %v\n", target_url)

	resp, err := http.Get(target_url)
	if err != nil {
		return fiber.NewError(400, err.Error())
	}
	defer resp.Body.Close()

	body, err := io.ReadAll(resp.Body)
	if err != nil {
		return fiber.NewError(400, err.Error())
	}

	var data MessageData
	if err := json.Unmarshal(body, &data); err != nil {
		return fiber.NewError(400, err.Error())
	}
	return c.JSON(data)
}

func GetMessage(c *fiber.Ctx) error {
	target_url := "http://laba2:24110" + c.OriginalURL()

	resp, err := http.Get(target_url)
	if err != nil {
		return fiber.NewError(400, err.Error())
	}
	defer resp.Body.Close()

	body, err := io.ReadAll(resp.Body)
	if err != nil {
		return fiber.NewError(400, err.Error())
	}

	var data MessageData
	if err := json.Unmarshal(body, &data); err != nil {
		return fiber.NewError(400, err.Error())
	}

	return c.JSON(data)
}
