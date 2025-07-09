package in.tech_camp.protospace_b;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import in.tech_camp.protospace_b.custom_user.CustomUserDetail;
import in.tech_camp.protospace_b.entity.UserEntity;
import in.tech_camp.protospace_b.repository.UserDetailRepository;
import lombok.RequiredArgsConstructor;

@ControllerAdvice
@RequiredArgsConstructor
public class UserModelAttribute {

    private final UserDetailRepository userDetailRepository;

    // ログインしていれば自動でユーザー情報をモデルに渡す
    @ModelAttribute
    public void addUserToModel(@AuthenticationPrincipal CustomUserDetail currentUser, Model model) {
        if (currentUser != null) {
            UserEntity user = userDetailRepository.findById(currentUser.getId());
            model.addAttribute("user", user);
            model.addAttribute("userRole", user.getRoleName());
        }
    }
}
