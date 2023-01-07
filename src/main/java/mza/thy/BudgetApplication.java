package mza.thy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@SpringBootApplication
@EnableAsync
@Controller
//@EnableAspectJAutoProxy
public class BudgetApplication {

    public static void main(String[] args) {

       // System.out.println("Input args: "+args[0]);

        System.out.println("HOW TO RUN: java -jar -DDB.FIRST_TIME=false -DDB.URL=jdbc:hsqldb:file:C:\\gosia\\development\\budget\\db\\budget budget-1.0.jar");

        SpringApplication.run(BudgetApplication.class, args);
    }
    @GetMapping("/")
    public String start(Model model) {
        return "start";
    }
}
