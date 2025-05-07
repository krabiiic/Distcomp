package com.dc.model.blo;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "tbl_message")
public class Message {
    @Id
    private Long id;

    private Long news;

    @Size(min = 2, max = 2048)
    private String content;
}
