package az.project.securityConfiguration.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/controller")
public class Controller {

    @GetMapping("/admin")
    public String getAdmin(){
        return "ADMIN";
    }

    @GetMapping("/client")
    public String getClient(){
        return "CLIENT";
    }

    @GetMapping("/any")
    public String getAny(){
        return "any";
    }
}
