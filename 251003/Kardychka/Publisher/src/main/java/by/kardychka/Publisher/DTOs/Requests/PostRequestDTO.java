package by.kardychka.Publisher.DTOs.Requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


public class PostRequestDTO {

    public PostRequestDTO(Long id){
        this.id = id;
    }

    public PostRequestDTO(){

    }

    private Long id;

    @NotNull(message = "News id can't be null")
    private Long newsId;

    @NotBlank(message = "Content may not be blank")
    @Size(min = 2, max = 2048, message = "Content should be between 2 and 2048 symbols")
    private String content;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNewsId() {
        return newsId;
    }

    public void setNewsId(Long newsId) {
        this.newsId = newsId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
