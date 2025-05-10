package lab.controllers;

import lab.exceptions.DataObjectNotFoundException;
import lab.exceptions.ForbiddenObjectException;
import lab.exceptions.IllegalFieldDataException;
import lab.mappers.IssueMapper;
import lab.modelDTOs.IssueRequestTo;
import lab.modelDTOs.IssueResponseTo;
import lab.models.Issue;
import lab.models.User;
import lab.services.IssueService;
import lab.services.TagService;
import lab.services.UserService;
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
    @Autowired
    private UserService userService;
    @Autowired
    private TagService tagService;

    @GetMapping(value = "")
    ResponseEntity<List<IssueResponseTo>> GetIssues(){
        List<IssueResponseTo> respList = new ArrayList<>();
        for (Issue issue : issueService.getIssues()){
            respList.add(IssueMapper.INSTANCE.MapIssueToResponseDTO(issue));
        }
        return new ResponseEntity<>(respList, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    ResponseEntity<IssueResponseTo> GetIssueById(@PathVariable("id") long id){
            Issue i = issueService.findById(id);
            return new ResponseEntity<>(IssueMapper.INSTANCE.MapIssueToResponseDTO(i), HttpStatus.OK);
    }

    @PostMapping(value = "")
    ResponseEntity<IssueResponseTo> CreateIssue(@RequestBody IssueRequestTo newIssue){
        Issue newI = IssueMapper.INSTANCE.MapRequestDTOToIssue(newIssue, tagService);
        Issue createdIssue = issueService.createIssue(newI);
        return new ResponseEntity<>(IssueMapper.INSTANCE.MapIssueToResponseDTO(createdIssue), HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/{id}")
    ResponseEntity<IssueResponseTo> DeleteIssue(@PathVariable("id") long Id){
        issueService.deleteIssue(Id);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    @PutMapping(value = "")
    ResponseEntity<IssueResponseTo> UpdateIssue(@RequestBody IssueRequestTo updIssue){
        long Id = updIssue.getId();
        Issue newI = IssueMapper.INSTANCE.MapRequestDTOToIssue(updIssue, tagService);
        Issue updatedIssue = issueService.updateIssue(newI);
        return new ResponseEntity<>(IssueMapper.INSTANCE.MapIssueToResponseDTO(updatedIssue), HttpStatus.OK);
    }
}
