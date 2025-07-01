package in.tech_camp.protospace_b.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import in.tech_camp.protospace_b.entity.UserEntity;
import in.tech_camp.protospace_b.form.UserSearchForm;
import in.tech_camp.protospace_b.repository.UserSearchRepository;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class UserSearchController {

    private final UserSearchRepository userSearchRepository;

    @GetMapping("/users/search")
    public String searchUsers(@ModelAttribute("form") UserSearchForm form, Model model) {
        List<UserEntity> users;

        if (form.getKeyword() == null || form.getKeyword().isBlank()) {
            users = Collections.emptyList(); // 初期表示 or 空入力
        } else {
            users = userSearchRepository.searchByKeyword(form.getKeyword());
        }

        model.addAttribute("users", users);
        return "users/search";
    }
}
