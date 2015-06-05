package todo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@EnableAutoConfiguration
@Configuration
@ComponentScan
@ImportResource("classpath:spring-context.xml")
public class TodoApp {
    public static void main(String args[]) throws Exception {
        SpringApplication.run(TodoApp.class, args);
    }
}
