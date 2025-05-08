package org.example.Impl.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseTo {
    private Long id;
    public String content;
    private Long newsId;
}