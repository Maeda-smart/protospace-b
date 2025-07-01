package in.tech_camp.protospace_b.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import in.tech_camp.protospace_b.entity.UserEntity;
import in.tech_camp.protospace_b.form.UserForm;
import in.tech_camp.protospace_b.repository.UserSignUpRepository;
import in.tech_camp.protospace_b.service.UserSignUpService;
import in.tech_camp.protospace_b.validation.ValidationOrder;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class SignUpController {

    private final UserSignUpService userSignUpService;
    private final UserSignUpRepository userSignUpRepository;

    @GetMapping("/users/sign_up")
    public String getMethodName(Model model) {
        model.addAttribute("userForm", new UserForm());
        return "users/signUp";
    }

    @PostMapping("/user")
    public String signUp(@ModelAttribute("userForm") @Validated(ValidationOrder.class) UserForm userForm,
            BindingResult result, Model model) {
        if (userSignUpRepository.existsByEmail(userForm.getEmail())) {
            result.rejectValue("email", "error.user", "Email already exists");
        }
        if (result.hasErrors()) {
            List<String> errorMessages = result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            model.addAttribute("errorMessages", errorMessages);
            model.addAttribute("userForm", userForm);
            return "users/signUp";
        }
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(userForm.getEmail());
        userEntity.setPassword(userForm.getPassword());
        userEntity.setNickname(userForm.getNickname());
        userEntity.setProfile(userForm.getProfile());
        userEntity.setAffiliation(userForm.getAffiliation());
        userEntity.setPosition(userForm.getPosition());
        if (userEntity.getRoleName() == null) {
            userEntity.setRoleName("ROLE_USER");
        }

        try {
            userSignUpService.createUserWithEncryptedPassword(userEntity);
        } catch (Exception e) {
            System.out.println("エラー:" + e);
            model.addAttribute("userForm", userForm);
            return "users/signUp";
        }
        return "redirect:/";
    }
}
