package in.tech_camp.protospace_b.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @GetMapping("/users/login")
    public String showLogin() {
        // model.addAttribute("loginForm", new LoginForm());
        return "users/login";
    }

    // ログインエラー時の表示処理
    @GetMapping("/login")
    public String showLoginWithError(@RequestParam(value = "error", required = false) String error, Model model) {
        // model.addAttribute("loginForm", new LoginForm());
        if (error != null) {
            model.addAttribute("loginError", "Invalid email or password.");
        }
        return "users/login";
    }
}