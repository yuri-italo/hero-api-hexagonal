package br.com.gubee;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = "br.com.gubee")
@EnableTransactionManagement
public class Application {

}
