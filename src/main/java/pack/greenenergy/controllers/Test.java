package pack.greenenergy.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@RequestMapping("/test")
public class Test {

    @GetMapping
    public String test() {
        return "You are testing a public end point";
    }

    @GetMapping("/private")
    public String test(Principal principal) {

        return "If you see this you are authenticated as " + principal.getName();
    }

}
