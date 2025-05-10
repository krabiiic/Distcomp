package by.zdanovich.Publisher.DTOs.Responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class IssueResponseDTO implements Serializable {
    private long id;
    private Long writerId;
    private String title;
    private String content;
    private Date created;
    private Date modified;
    private List<String> marks;
}
