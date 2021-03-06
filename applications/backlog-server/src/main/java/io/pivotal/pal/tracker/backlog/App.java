package io.pivotal.pal.tracker.backlog;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestOperations;

import java.util.*;


@EnableCircuitBreaker
@EnableEurekaClient
@SpringBootApplication
@ComponentScan({"io.pivotal.pal.tracker.backlog", "io.pivotal.pal.tracker.restsupport"})
@RestController
@EnableResourceServer
@EnableWebSecurity
public class App {

    public static void main(String[] args) {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
        SpringApplication.run(App.class, args);
    }

    @Bean
    ProjectClient projectClient(
        RestOperations restOperations,
        @Value("${registration.server.endpoint}") String registrationEndpoint
    ) {
        return new ProjectClient(restOperations, registrationEndpoint);
    }

    @GetMapping("/health")
    public Map<String,Object> health() {
        HashMap<String, Object> healthMap = new HashMap<>();
        healthMap.put("hystrix", Health.status(Status.UP).build());
        healthMap.put("configServer", Health.status(Status.UP).build());
        healthMap.put("status", "UP");
        return healthMap;
    }

    @GetMapping("/beans")
    public List<Map<String, List<Map<String, String>>>> beans() {
        List<Map<String, List<Map<String, String>>>> output = new ArrayList<>();
        output.add(new HashMap<>());
        output.get(0).put("beans", new ArrayList<>());
        output.get(0).get("beans").add(new HashMap<>());
        output.get(0).get("beans").get(0).put("bean", "loadBalancerClient");
        return output;
    }
}
