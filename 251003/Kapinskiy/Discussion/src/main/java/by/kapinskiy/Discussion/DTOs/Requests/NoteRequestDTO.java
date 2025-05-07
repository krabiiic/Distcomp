package by.kapinskiy.Discussion.DTOs.Requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class NoteRequestDTO {
    private Long id;

    private Long issueId;

    private String content;
}
