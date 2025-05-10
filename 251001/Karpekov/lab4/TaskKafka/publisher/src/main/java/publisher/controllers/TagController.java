package publisher.controllers;

import publisher.mappers.TagMapper;
import publisher.modelDTOs.TagRequestTo;
import publisher.modelDTOs.TagResponseTo;
import publisher.models.Tag;
import publisher.services.TagService;
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
        for (Tag tag : tagService.getTags()){
            respList.add(TagMapper.INSTANCE.MapTagToResponseDTO(tag));
        }
        return new ResponseEntity<>(respList, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    ResponseEntity<TagResponseTo> GetTagById(@PathVariable("id") long id){
        Tag t = tagService.findById(id);
        return new ResponseEntity<>(TagMapper.INSTANCE.MapTagToResponseDTO(t), HttpStatus.OK);
    }

    @PostMapping(value = "")
    ResponseEntity<TagResponseTo> CreateTag(@RequestBody TagRequestTo newTag){
        Tag newU = TagMapper.INSTANCE.MapRequestDTOToTag(newTag);
        Tag createdTag = tagService.createTag(newU);
        return new ResponseEntity<>(TagMapper.INSTANCE.MapTagToResponseDTO(createdTag), HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{id}")
    ResponseEntity<TagResponseTo> DeleteTag(@PathVariable("id") long Id){
        tagService.deleteTag(Id);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    @PutMapping(value = "")
    ResponseEntity<TagResponseTo> UpdateTag(@RequestBody TagRequestTo updTag){
        long Id = updTag.getId();
        Tag newT = TagMapper.INSTANCE.MapRequestDTOToTag(updTag);
        Tag updatedTag = tagService.updateTag(newT);
        return new ResponseEntity<>(TagMapper.INSTANCE.MapTagToResponseDTO(updatedTag), HttpStatus.OK);
    }
}
