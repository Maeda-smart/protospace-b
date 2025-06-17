package in.tech_camp.protospace_b.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.AllArgsConstructor;


@Controller
@AllArgsConstructor
public class signUpViewController {
  @GetMapping("/users/sign_up")
  public String getMethodName(Model model) {
      return "users/signUp.html";
  }
  
}
