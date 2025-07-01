package in.tech_camp.protospace_b.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import in.tech_camp.protospace_b.form.LoginForm;

@Controller
public class LoginController {

    @GetMapping("/users/login")
    public String showLogin(@RequestParam(value = "error", required = false) String error, Model model) {
        model.addAttribute("loginForm", new LoginForm());
        if (error != null) {
            model.addAttribute("loginError", "Invalid email or password");
        }
        return "users/login";
    }
}
