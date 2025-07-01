package in.tech_camp.protospace_b.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import in.tech_camp.protospace_b.entity.UserEntity;
import in.tech_camp.protospace_b.form.UserEditForm;
import in.tech_camp.protospace_b.repository.UserEditRepository;
import in.tech_camp.protospace_b.validation.ValidationOrder;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class UserEditController {

    private final UserEditRepository userEditRepository;

    private final PasswordEncoder passwordEncoder;

    @GetMapping("/users/{userId}/edit")
    public String editUserForm(@PathVariable("userId") Integer userId, Model model) {
        UserEntity user = userEditRepository.findById(userId);

        UserEditForm userForm = new UserEditForm();
        userForm.setId(user.getId());
        userForm.setNickname(user.getNickname());
        userForm.setEmail(user.getEmail());
        userForm.setProfile(user.getProfile());
        userForm.setAffiliation(user.getAffiliation());
        userForm.setPosition(user.getPosition());

        model.addAttribute("userForm", userForm);
        return "users/edit";
    }

    @PostMapping("/users/{userId}")
    public String updateUser(@PathVariable("userId") Integer userId,
            @ModelAttribute("userForm") @Validated(ValidationOrder.class) UserEditForm userEditForm,
            BindingResult result, Model model) {
        String newEmail = userEditForm.getEmail();
        if (userEditRepository.existsByEmailExcludingCurrent(newEmail, userId)) {
            result.rejectValue("email", "error.user", "Email already exists");
        }
        if (result.hasErrors()) {
            List<String> errorMessages = result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            model.addAttribute("errorMessages", errorMessages);
            model.addAttribute("userForm", userEditForm);
            return "users/edit";
        }

        // String encodedPassword = null;
        // if (userEditForm.getPassword() != null &&
        // !userEditForm.getPassword().isEmpty()) {
        // encodedPassword = passwordEncoder.encode(userEditForm.getPassword());
        // }

        UserEntity user = userEditRepository.findById(userId);
        user.setNickname(userEditForm.getNickname());
        user.setEmail(userEditForm.getEmail());
        // user.setPassword(userEditForm.getPassword());
        user.setProfile(userEditForm.getProfile());
        user.setAffiliation(userEditForm.getAffiliation());
        user.setPosition(userEditForm.getPosition());

        if (userEditForm.getPassword() != null && !userEditForm.getPassword().isEmpty()) {
            String encodedPassword = passwordEncoder.encode(userEditForm.getPassword());
            user.setPassword(encodedPassword);
        }

        try {
            userEditRepository.update(user);
        } catch (Exception e) {
            System.out.println("エラー：" + e);
            model.addAttribute("userForm", userEditForm);
            return "users/edit";
        }
        return "redirect:/";
    }
}
