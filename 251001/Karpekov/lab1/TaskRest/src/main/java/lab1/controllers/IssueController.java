package lab1.controllers;

import lab1.Mappers.IssueMapper;
import lab1.modelDTOs.IssueRequestTo;
import lab1.modelDTOs.IssueResponseTo;
import lab1.models.Issue;
import lab1.services.IssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "api/v1.0/issues", consumes = {"application/JSON"}, produces = {"application/JSON"})
public class IssueController {

    @Autowired
    private IssueService issueService;

    @GetMapping(value = "")
    ResponseEntity<List<IssueResponseTo>> GetIssues(){
        List<IssueResponseTo> respList = new ArrayList<>();
        for (Issue issue : issueService.GetIssues()){
            respList.add(IssueMapper.INSTANCE.MapIssueToResponseDTO(issue));
        }
        return new ResponseEntity<>(respList, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    ResponseEntity<IssueResponseTo> GetIssueById(@PathVariable("id") long id){
        Issue i = issueService.GetIssueById(id);
        if (i != null)
            return new ResponseEntity<>(IssueMapper.INSTANCE.MapIssueToResponseDTO(i), HttpStatus.OK);
        else
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @PostMapping(value = "")
    ResponseEntity<IssueResponseTo> CreateIssue(@RequestBody IssueRequestTo newIssue){
        Issue newU = IssueMapper.INSTANCE.MapRequestDTOToIssue(newIssue);
        boolean added = issueService.CreateIssue(newU);
        if (added)
            return new ResponseEntity<>(IssueMapper.INSTANCE.MapIssueToResponseDTO(newU), HttpStatus.CREATED);
        else
            return new ResponseEntity<>(null, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @DeleteMapping(value = "/{id}")
    ResponseEntity<IssueResponseTo> DeleteIssue(@PathVariable("id") long Id){
        Issue del = issueService.DeleteIssue(Id);
        if (del == null){
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }else {
            return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
        }
    }

    @PutMapping(value = "")
    ResponseEntity<IssueResponseTo> UpdateIssue(@RequestBody IssueRequestTo updIssue){
        long Id = updIssue.getId();
        Issue newI = IssueMapper.INSTANCE.MapRequestDTOToIssue(updIssue);
        Issue updated = issueService.UpdateIssue(newI);
        if (updated != null)
            return new ResponseEntity<>(IssueMapper.INSTANCE.MapIssueToResponseDTO(newI), HttpStatus.OK);
        else
            return new ResponseEntity<>(IssueMapper.INSTANCE.MapIssueToResponseDTO(newI), HttpStatus.NOT_FOUND);
    }

}
