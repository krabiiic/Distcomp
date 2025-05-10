package discussion;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@SpringBootApplication
@EnableTransactionManagement
@EnableAutoConfiguration
@EnableCassandraRepositories("discussion.repository")
@ComponentScan("discussion")
@EntityScan("discussion.models")
public class DiscussionApplication {

	public static void main(String[] args) {
		SpringApplication.run(DiscussionApplication.class, args);
	}
}
