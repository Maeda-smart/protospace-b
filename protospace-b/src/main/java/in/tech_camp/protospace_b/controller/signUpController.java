package in.tech_camp.protospace_b.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import in.tech_camp.protospace_b.form.UserForm;
import lombok.AllArgsConstructor;



@Controller
@AllArgsConstructor
public class signUpController {
  @GetMapping("/users/sign_up")
  public String getMethodName(Model model) {
      model.addAttribute("userForm", new UserForm());
      return "users/signUp.html";
  }
  @PostMapping("/user")
  public String postMethodName(@ModelAttribute("userForm") UserForm userForm, BindingResult result, Model model) {
      return "redirect:/";
  }
  
  
}
