package in.tech_camp.protospace_b.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import in.tech_camp.protospace_b.form.LoginForm;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class LoginController {

    @GetMapping("/users/login")
    public String showLogin(@RequestParam(value = "error", required = false) String error, Model model, HttpServletRequest request) {
        model.addAttribute("loginForm", new LoginForm());
        
        if(error != null){
        Object errorMessage = request.getSession().getAttribute("error");
        System.out.println("Error: " + error);
        if (errorMessage != null) {
            System.out.println("Error: " + errorMessage);
            model.addAttribute("error", errorMessage);
            request.getSession().removeAttribute("error"); // 一度表示したら消す
        }
    }
        return "users/login";
    }
}
