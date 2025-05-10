package storage

import (
	"context"
	"distributedcomputing/model"
	"errors"
	"fmt"
	"sync"

	"go.mongodb.org/mongo-driver/bson"
	"go.mongodb.org/mongo-driver/bson/primitive"
	"go.mongodb.org/mongo-driver/mongo"
)

type NoteStorage struct {
	collection *mongo.Collection
	idMap      map[int64]primitive.ObjectID
	idCounter  int64
	mutex      sync.Mutex
}

func NewNoteStorage(client *mongo.Client, dbName, collectionName string) *NoteStorage {
	collection := client.Database(dbName).Collection(collectionName)
	return &NoteStorage{
		collection: collection,
		idMap:      make(map[int64]primitive.ObjectID),
		idCounter:  1,
	}
}

func (s *NoteStorage) Create(note model.Note) (int64, error) {
	s.mutex.Lock()
	defer s.mutex.Unlock()

	id := note.Id
	result, err := s.collection.InsertOne(context.TODO(), note)
	
	if err != nil {
		fmt.Println("laladlvnfjk: ", err)
		return 0, err
	}

	oid, ok := result.InsertedID.(primitive.ObjectID)
	if !ok {
		return 0, errors.New("failed to convert InsertedID to ObjectID")
	}

	s.idMap[id] = oid	
	return id, nil
}

func (s *NoteStorage) Get(id int64) (model.Note, error) {
	var note model.Note
	oid, exists := s.idMap[id]
	if !exists {
		return note, errors.New("ID not found")
	}

	filter := bson.M{"_id": oid}
	err := s.collection.FindOne(context.TODO(), filter).Decode(&note)
	return note, err
}

func (s *NoteStorage) GetAll() ([]model.Note, []int64, error) {
	var notes []model.Note
	var ids []int64

	cursor, err := s.collection.Find(context.TODO(), bson.M{})
	if err != nil {
		return nil, nil, err
	}
	defer cursor.Close(context.TODO())

	for cursor.Next(context.TODO()) {
		var note model.Note
		if err := cursor.Decode(&note); err != nil {
			return nil, nil, err
		}
		notes = append(notes, note)
		ids = append(ids, note.Id)
	}

	return notes, ids, nil
}

func (s *NoteStorage) Update(id int64, note model.Note) error {
	s.mutex.Lock()
	defer s.mutex.Unlock()
	oid, exists := s.idMap[id]
	if !exists {
		return errors.New("ID not found")
	}

	filter := bson.M{"_id": oid}
	update := bson.M{"$set": note}
	_, err := s.collection.UpdateOne(context.TODO(), filter, update)
	return err
}

func (s *NoteStorage) Delete(id int64) error {
	s.mutex.Lock()
	defer s.mutex.Unlock()
	oid, exists := s.idMap[id]
	if !exists {
		return errors.New("ID not found")
	}

	filter := bson.M{"_id": oid}
	res, err := s.collection.DeleteOne(context.TODO(), filter)
	if err != nil {
		return err
	}
	if res.DeletedCount == 0 {
		return errors.New("no document deleted")
	}

	delete(s.idMap, id)
	return nil
}