package org.example.kafka;


import org.example.controller.PostController;
import org.example.dto.PostRequestTo;
import org.example.dto.PostResponseTo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {

    @Autowired
    PostController postController;

    @Autowired
    KafkaProducer kafkaProducer;

    @KafkaListener(topics = "InTopic", groupId = "my_group",
            containerFactory = "postRequestToConcurrentKafkaListenerContainerFactory")
    public void listen(@Payload PostRequestTo postRequestTo) {
        String method = postRequestTo.getMethod();
        System.out.println("---Received message = " + postRequestTo.toString());
        switch (method) {
            case "GET":
                if (postRequestTo.getId() != null) {
                    kafkaProducer.send(postController.getPost(postRequestTo.getId()), "OutTopic");
                }
                else postController.getPosts();
                break;
            case "POST":
                kafkaProducer.send(postController.savePost(postRequestTo.getCountry(), postRequestTo), "OutTopic");
                break;
            case "PUT":
                kafkaProducer.send(postController.updatePost(postRequestTo.getCountry(), postRequestTo), "OutTopic");
                break;
            case "DELETE":
                kafkaProducer.send(postController.deletePost(postRequestTo.getId()), "OutTopic");
                break;
            default:
                break;
        }
    }

}
