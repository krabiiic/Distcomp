package controllers

import (
	"math/rand"
	"fmt"
	"github.com/labstack/echo/v4"
	"distributedcomputing/model"
	"distributedcomputing/service"
	"net/http"
	"strconv"
	"errors"
)

type NoteController struct {
	service *service.NoteService
}

func WrapErr(err error) map[string]string {
	return map[string]string{"error": err.Error()}
}


func NewNoteController(service *service.NoteService) *NoteController {
	return &NoteController{service: service}
}

func (nc *NoteController) Create(c echo.Context) error {
	var dto model.NoteRequestTo
	if err := c.Bind(&dto); err != nil {
		return c.JSON(http.StatusBadRequest, WrapErr(err))
	}
	dto.Id = rand.Uint64()
	if err := validateNote(dto); err != nil {
		return c.JSON(http.StatusBadRequest, WrapErr(err))
	}

	fmt.Println("create ", dto)
	note, err := nc.service.Create(dto)
	if err != nil {
		fmt.Println(err)
		fmt.Println("okkkk")
		return c.JSON(http.StatusForbidden, WrapErr(err))
	}
	fmt.Println("no error...")
	return c.JSON(http.StatusCreated, note)
}

func (nc *NoteController) Get(c echo.Context) error {
	fmt.Println("get")
	idStr := c.Param("id")
	id, err := strconv.ParseInt(idStr, 10, 64)
	if err != nil {
		return c.JSON(http.StatusBadRequest, "Invalid ID format")
	}

	note, err := nc.service.Get(id)
	if err != nil {
		return c.JSON(http.StatusNotFound, WrapErr(err))
	}

	return c.JSON(http.StatusOK, note)
}

func (nc *NoteController) GetAll(c echo.Context) error {
	fmt.Println("getall")
	notes, err := nc.service.GetAll()
	if err != nil {
		// fmt.Println("getall ", err, notes)
		return c.JSON(http.StatusNotFound, WrapErr(err))
	}
	// fmt.Println("notes")
	
	return c.JSON(http.StatusOK, notes)
}

func (nc *NoteController) Update(c echo.Context) error {
	fmt.Println("update")
	var dto model.NoteRequestTo
	if err := c.Bind(&dto); err != nil {
		return c.JSON(http.StatusBadRequest, WrapErr(err))
	}

	if err := validateNote(dto); err != nil {
		return c.JSON(http.StatusBadRequest, WrapErr(err))
	}

	if err := nc.service.Update(dto); err != nil {
		fmt.Println(err)
		return c.JSON(http.StatusForbidden, WrapErr(err))
	}

	return c.JSON(http.StatusOK, dto)
}

func (nc *NoteController) Delete(c echo.Context) error {
	fmt.Println("delete")
	idStr := c.Param("id")
	id, err := strconv.ParseInt(idStr, 10, 64)
	if err != nil {
		return c.JSON(http.StatusBadRequest, "Invalid ID format")
	}

	if err := nc.service.Delete(id); err != nil {
		return c.NoContent(http.StatusNotFound)
	}

	return c.NoContent(http.StatusNoContent)
}




func validateNote(dto model.NoteRequestTo) error {
	if len(dto.Content) < 2 || len(dto.Content) > 2048 {
		return errors.New("note content must be between 2 and 2048 characters")
	}
	return nil
}
