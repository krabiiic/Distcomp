package by.ikrotsyuk.bsuir.modulediscussion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories(basePackages = "by.ikrotsyuk.bsuir.modulediscussion.repository")
public class ModuleDiscussionApplication {

    public static void main(String[] args) {
        SpringApplication.run(ModuleDiscussionApplication.class, args);
    }

}
