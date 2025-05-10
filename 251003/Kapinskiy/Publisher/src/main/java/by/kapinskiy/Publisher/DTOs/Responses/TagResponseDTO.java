package by.kapinskiy.Publisher.DTOs.Responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TagResponseDTO implements Serializable {
    private Long id;
    private String name;
}
