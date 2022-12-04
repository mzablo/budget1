package mza.thy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
//@EnableAspectJAutoProxy
public class BudgetApplication {

    public static void main(String[] args) {

        System.out.println(" "+args[0]);
        SpringApplication.run(BudgetApplication.class, args);
    }
}
