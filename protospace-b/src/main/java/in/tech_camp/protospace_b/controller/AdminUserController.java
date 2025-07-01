package in.tech_camp.protospace_b.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import in.tech_camp.protospace_b.entity.UserEntity;
import in.tech_camp.protospace_b.repository.AdminUserRepository;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class AdminUserController {

    private final AdminUserRepository adminUserRepository;

    // ユーザー管理ページに遷移
    @GetMapping("/admin/users")
    public String showUserList(Model model) {
        List<UserEntity> userList = adminUserRepository.findAllUsers();
        model.addAttribute("userList", userList);
        return "admin/userList";
    }

    // ユーザー凍結機能
    @PostMapping("/admin/users/{userId}/freeze")
     public String disableUser(@PathVariable("userId") Integer userId, Integer id) {
        UserEntity user = adminUserRepository.findById(userId);
        if (user != null){
            user.setEnable(false);
            adminUserRepository.freezeUserById(userId);
        }
        return "redirect:/admin/users";
    }
}
