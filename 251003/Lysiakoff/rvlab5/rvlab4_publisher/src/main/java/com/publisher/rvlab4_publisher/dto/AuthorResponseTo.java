package com.publisher.rvlab4_publisher.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthorResponseTo {
    private Long id;
    private String login;
    private String firstname;
    private String lastname;
    private List<Long> newsIds;

}