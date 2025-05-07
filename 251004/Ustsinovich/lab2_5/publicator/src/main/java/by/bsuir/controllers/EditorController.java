package by.bsuir.controllers;

import by.bsuir.dto.EditorRequestTo;
import by.bsuir.dto.EditorResponseTo;
import by.bsuir.services.EditorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1.0/editors")
public class EditorController {
    @Autowired
    EditorService editorService;

    @GetMapping
    public ResponseEntity<List<EditorResponseTo>> getEditors(
            @RequestParam(required = false, defaultValue = "0") Integer pageNumber,
            @RequestParam(required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(required = false, defaultValue = "id") String sortBy,
            @RequestParam(required = false, defaultValue = "desc") String sortOrder) {
        return ResponseEntity.status(200).body(editorService.getEditors(pageNumber, pageSize, sortBy, sortOrder));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EditorResponseTo> getEditor(@PathVariable Long id) {
        return ResponseEntity.status(200).body(editorService.getEditorById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEditor(@PathVariable Long id) {
        editorService.deleteEditor(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public ResponseEntity<EditorResponseTo> saveEditor(@RequestBody EditorRequestTo editor) {
        EditorResponseTo savedEditor = editorService.saveEditor(editor);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedEditor);
    }

    @PutMapping
    public ResponseEntity<EditorResponseTo> updateEditor(@RequestBody EditorRequestTo editor) {
        return ResponseEntity.status(HttpStatus.OK).body(editorService.updateEditor(editor));
    }

    @GetMapping("/byIssue/{id}")
    public ResponseEntity<EditorResponseTo> getEditorByIssueId(@PathVariable Long id){
        return ResponseEntity.status(200).body(editorService.getEditorByIssueId(id));
    }
}
