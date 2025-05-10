package by.bsuir.romamuhtasarov.impl.controllers;

import by.bsuir.romamuhtasarov.impl.service.CreatorService;
import by.bsuir.romamuhtasarov.impl.dto.CreatorResponseTo;
import by.bsuir.romamuhtasarov.impl.dto.CreatorRequestTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1.0", consumes = {"application/JSON"}, produces = {"application/JSON"})
public class CreatorController {

    @Autowired
    private CreatorService creatorService;

    @GetMapping("/creators")
    public ResponseEntity<List<CreatorResponseTo>> getAllCreators() {
        List<CreatorResponseTo> creatorResponseToList = creatorService.getAll();
        return new ResponseEntity<>(creatorResponseToList, HttpStatus.OK);
    }

    @GetMapping("/creators/{id}")
    public ResponseEntity<CreatorResponseTo> getCreator(@PathVariable long id) {
        CreatorResponseTo creatorResponseTo = creatorService.get(id);
        return new ResponseEntity<>(creatorResponseTo, creatorResponseTo == null ? HttpStatus.NOT_FOUND : HttpStatus.OK);
    }

    @PostMapping("/creators")
    public ResponseEntity<CreatorResponseTo> createCreator(@RequestBody CreatorRequestTo CreatorTo) {
        CreatorResponseTo addedCreator = creatorService.add(CreatorTo);
        return new ResponseEntity<>(addedCreator, HttpStatus.CREATED);
    }

    @DeleteMapping("/creators/{id}")
    public ResponseEntity<CreatorResponseTo> deleteCreator(@PathVariable long id) {
        CreatorResponseTo deletedCreator = creatorService.delete(id);
        return new ResponseEntity<>(deletedCreator, deletedCreator == null ? HttpStatus.NOT_FOUND : HttpStatus.NO_CONTENT);
    }

    @PutMapping("/creators")
    public ResponseEntity<CreatorResponseTo> updateCreator(@RequestBody CreatorRequestTo creatorRequestTo) {
        CreatorResponseTo creatorResponseTo = creatorService.update(creatorRequestTo);
        return new ResponseEntity<>(creatorResponseTo, creatorResponseTo.getFirstname() == null ? HttpStatus.NOT_FOUND : HttpStatus.OK);
    }

}