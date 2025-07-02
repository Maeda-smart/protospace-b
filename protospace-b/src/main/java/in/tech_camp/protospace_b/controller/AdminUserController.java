package in.tech_camp.protospace_b.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import in.tech_camp.protospace_b.custom_user.CustomUserDetail;
import in.tech_camp.protospace_b.entity.UserEntity;
import in.tech_camp.protospace_b.repository.AdminUserRepository;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class AdminUserController {

    private final AdminUserRepository adminUserRepository;

    // ユーザー管理ページに遷移
    @GetMapping("/moderate/users")
    public String showUserList(@AuthenticationPrincipal CustomUserDetail currentUser ,Model model) {
        Integer loginUserId = currentUser.getId();
        List<UserEntity> userList = adminUserRepository.findAllUsers(loginUserId);
        model.addAttribute("userList", userList);
        return "moderate/userList";
    }

    // アカウント凍結機能
    @PostMapping("/moderate/users/{userId}/freeze")
     public String freezeUser(@PathVariable("userId") Integer userId, Integer id) {
        UserEntity user = adminUserRepository.findById(userId);
        if (user != null){
            user.setEnable(false);
            adminUserRepository.freezeUserById(userId);
        }
        return "redirect:/moderate/users";
    }

    // アカウント凍結解除
    @PostMapping("/moderate/users/{userId}/unfreeze")
     public String unfreezeUser(@PathVariable("userId") Integer userId, Integer id) {
        UserEntity user = adminUserRepository.findById(userId);
        if (user != null){
            user.setEnable(true);
            adminUserRepository.unfreezeUserById(userId);
        }
        return "redirect:/moderate/users";
    }

    // モデレーター権限付与
    @PostMapping("/admin/users/{userId}/promote")
    public String promoteUser(@PathVariable("userId") Integer userId, Integer id) {
        UserEntity user = adminUserRepository.findById(userId);
        if (user != null){
            user.setRoleName("ROLE_MODERATOR");
            adminUserRepository.promoteUserById(userId);
        }
        return "redirect:/moderate/users";
    }

    // モデレーター権限剝奪
    @PostMapping("/admin/users/{userId}/demote")
    public String demoteUser(@PathVariable("userId") Integer userId, Integer id) {
        UserEntity user = adminUserRepository.findById(userId);
        if (user != null){
            user.setRoleName("ROLE_USER");
            adminUserRepository.demoteUserById(userId);
        }
        return "redirect:/moderate/users";
    }
}
