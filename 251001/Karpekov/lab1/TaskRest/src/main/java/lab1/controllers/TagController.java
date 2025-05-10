package lab1.controllers;

import lab1.Mappers.TagMapper;
import lab1.modelDTOs.TagRequestTo;
import lab1.modelDTOs.TagResponseTo;
import lab1.models.Tag;
import lab1.services.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "api/v1.0/tags", consumes = {"application/JSON"}, produces = {"application/JSON"})
public class TagController {

    @Autowired
    private TagService tagService;

    @GetMapping(value = "")
    ResponseEntity<List<TagResponseTo>> GetTags(){
        List<TagResponseTo> respList = new ArrayList<>();
        for (Tag tag : tagService.GetTags()){
            respList.add(TagMapper.INSTANCE.MapTagToResponseDTO(tag));
        }
        return new ResponseEntity<>(respList, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    ResponseEntity<TagResponseTo> GetTagById(@PathVariable("id") long id){
        Tag t = tagService.GetTagById(id);
        if (t != null)
            return new ResponseEntity<>(TagMapper.INSTANCE.MapTagToResponseDTO(t), HttpStatus.OK);
        else
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @PostMapping(value = "")
    ResponseEntity<TagResponseTo> CreateTag(@RequestBody TagRequestTo newTag){
        Tag newU = TagMapper.INSTANCE.MapRequestDTOToTag(newTag);
        boolean added = tagService.CreateTag(newU);
        if (added)
            return new ResponseEntity<>(TagMapper.INSTANCE.MapTagToResponseDTO(newU), HttpStatus.CREATED);
        else
            return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @DeleteMapping(value = "/{id}")
    ResponseEntity<TagResponseTo> DeleteTag(@PathVariable("id") long Id){
        Tag del = tagService.DeleteTag(Id);
        if (del == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }else {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }
    }

    @PutMapping(value = "")
    ResponseEntity<TagResponseTo> UpdateTag(@RequestBody TagRequestTo updTag){
        long Id = updTag.getId();
        Tag newT = TagMapper.INSTANCE.MapRequestDTOToTag(updTag);
        Tag updated = tagService.UpdateTag(newT);
        if (updated != null)
            return new ResponseEntity<>(TagMapper.INSTANCE.MapTagToResponseDTO(newT), HttpStatus.OK);
        else
            return new ResponseEntity<>(TagMapper.INSTANCE.MapTagToResponseDTO(newT), HttpStatus.NOT_FOUND);
    }

}
