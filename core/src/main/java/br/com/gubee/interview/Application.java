package br.com.gubee.interview;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = "br.com.gubee")
@EnableTransactionManagement
//@Import({JdbcConfiguration.class, WebConfiguration.class, RegisterHeroController.class})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
