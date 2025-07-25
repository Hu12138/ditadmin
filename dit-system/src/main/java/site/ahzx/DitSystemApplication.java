package site.ahzx;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("site.ahzx.mapper")
public class DitSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(DitSystemApplication.class, args);
    }

}
