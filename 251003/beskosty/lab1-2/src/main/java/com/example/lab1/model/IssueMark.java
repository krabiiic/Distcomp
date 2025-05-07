package com.example.lab1.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "tbl_issue_mark")
public class IssueMark {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "issue_id", nullable = false)
    @JsonIgnore
    private Issue issue;

    @ManyToOne
    @JoinColumn(name = "mark_id", nullable = false)
    @JsonIgnore
    private Mark mark;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Issue getIssue() { return issue; }
    public void setIssue(Issue issue) { this.issue = issue; }
    
    public Mark getMark() { return mark; }
    public void setMark(Mark mark) { this.mark = mark; }
}
