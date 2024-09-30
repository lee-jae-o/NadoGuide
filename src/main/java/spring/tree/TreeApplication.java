package spring.tree;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@EnableFeignClients
@EnableRedisHttpSession
@EnableJpaRepositories
@SpringBootApplication
public class TreeApplication {

	public static void main(String[] args) {
		SpringApplication.run(TreeApplication.class, args);
	}

}
