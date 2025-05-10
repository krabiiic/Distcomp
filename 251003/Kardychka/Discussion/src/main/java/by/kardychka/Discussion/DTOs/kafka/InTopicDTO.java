package by.kardychka.Discussion.DTOs.kafka;

import by.kardychka.Discussion.DTOs.Requests.PostRequestDTO;

public class InTopicDTO {
    private String method;
    private PostRequestDTO postRequestDTO;
    private String status;

    public InTopicDTO(String method, PostRequestDTO postRequestDTO, String status) {
        this.method = method;
        this.postRequestDTO = postRequestDTO;
        this.status = status;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public PostRequestDTO getPostRequestDTO() {
        return postRequestDTO;
    }

    public void setPostRequestDTO(PostRequestDTO postRequestDTO) {
        this.postRequestDTO = postRequestDTO;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public InTopicDTO() {
    }
}
