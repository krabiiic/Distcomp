package by.bsuir.romamuhtasarov.impl.controllers;

import by.bsuir.romamuhtasarov.impl.service.LabelService;
import by.bsuir.romamuhtasarov.impl.dto.LabelRequestTo;
import by.bsuir.romamuhtasarov.impl.dto.LabelResponseTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1.0")
public class LabelController {
    @Autowired
    private LabelService LabelService;

    @GetMapping("/labels")
    public ResponseEntity<List<LabelResponseTo>> getAllLabels() {
        List<LabelResponseTo> LabelResponseToList = LabelService.getAll();
        return new ResponseEntity<>(LabelResponseToList, HttpStatus.OK);
    }

    @GetMapping("/labels/{id}")
    public ResponseEntity<LabelResponseTo> getLabel(@PathVariable long id) {
        LabelResponseTo LabelResponseTo = LabelService.get(id);
        return new ResponseEntity<>(LabelResponseTo, LabelResponseTo == null ? HttpStatus.NOT_FOUND : HttpStatus.OK);
    }

    @PostMapping("/labels")
    public ResponseEntity<LabelResponseTo> createLabel(@RequestBody LabelRequestTo LabelTo) {
        LabelResponseTo addedLabel = LabelService.add(LabelTo);
        return new ResponseEntity<>(addedLabel, HttpStatus.CREATED);
    }

    @DeleteMapping("/labels/{id}")
    public ResponseEntity<LabelResponseTo> deleteLabel(@PathVariable long id) {
        LabelResponseTo deletedLabel = LabelService.delete(id);
        return new ResponseEntity<>(deletedLabel, deletedLabel == null ? HttpStatus.NOT_FOUND : HttpStatus.NO_CONTENT);
    }

    @PutMapping("/labels")
    public ResponseEntity<LabelResponseTo> updateLabel(@RequestBody LabelRequestTo LabelRequestTo) {
        LabelResponseTo LabelResponseTo = LabelService.update(LabelRequestTo);
        return new ResponseEntity<>(LabelResponseTo, LabelResponseTo.getName() == null ? HttpStatus.NOT_FOUND : HttpStatus.OK);
    }
}