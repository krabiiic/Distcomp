package lab1.services;

import lab1.datalayer.DataLayer;
import lab1.datalayer.FieldChecker;
import lab1.datalayer.InMemoryDataLayer;
import lab1.models.Issue;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
public class IssueService {

    private final DataLayer dataLayer;
    public IssueService(){
        dataLayer = new InMemoryDataLayer();
    }

    public ArrayList<Issue> GetIssues(){
        return dataLayer.GetIssues();
    }

    public Issue GetIssueById(long Id) throws IllegalArgumentException{
        Issue res = dataLayer.GetIssue(Id);
        return res;
    }

    public boolean CreateIssue(Issue issue){
        if (FieldChecker.CheckIssueFields(issue)){
            dataLayer.CreateIssue(issue);
            return issue.getId() != -1;
        }else
            return false;
    }

    public Issue DeleteIssue(long Id){
        return dataLayer.DeleteIssue(Id);
    }

    public Issue UpdateIssue(Issue issue){
        if (FieldChecker.CheckIssueFields(issue))
            return dataLayer.UpdateIssue(issue);
        else
            return null;
    }

    public DataLayer getDataLayer(){
        return dataLayer;
    }

}
