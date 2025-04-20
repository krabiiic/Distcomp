package com.example.restservice.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Mark implements Identifiable {
    private Long id;
    private String name;
}
