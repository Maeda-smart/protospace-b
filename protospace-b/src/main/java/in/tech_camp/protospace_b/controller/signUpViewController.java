package in.tech_camp.protospace_b.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import in.tech_camp.protospace_b.form.UserForm;
import lombok.AllArgsConstructor;


@Controller
@AllArgsConstructor
public class signUpViewController {
  @GetMapping("/users/sign_up")
  public String getMethodName(Model model) {
      model.addAttribute("userForm", new UserForm());
      return "users/signUp.html";
  }
  
}
