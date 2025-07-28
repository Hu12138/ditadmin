package site.ahzx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class DitAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(DitAuthApplication.class, args);
    }

}
