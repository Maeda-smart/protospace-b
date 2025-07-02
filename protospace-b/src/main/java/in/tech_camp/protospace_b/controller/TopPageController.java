package in.tech_camp.protospace_b.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import in.tech_camp.protospace_b.custom_user.CustomUserDetail;
import in.tech_camp.protospace_b.entity.PrototypeEntity;
import in.tech_camp.protospace_b.form.PrototypeSearchForm;
import in.tech_camp.protospace_b.repository.PrototypeShowRepository;
import in.tech_camp.protospace_b.repository.UserDetailRepository;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class TopPageController {

    private final PrototypeShowRepository prototypeShowRepository;
    private final UserDetailRepository userDetailRepository;

    @GetMapping("")
    public String topPage(@AuthenticationPrincipal CustomUserDetail currentUser, Model model) {

        // ログイン時のみuserIdを取得
        Integer userId = (currentUser != null) ? currentUser.getId() : null;
        model.addAttribute("user", userDetailRepository.findById(userId));

        // 全プロトタイプ取得を取得し、モデルに渡す
        List<PrototypeEntity> prototypes = prototypeShowRepository.showAll(userId);
        model.addAttribute("prototypes", prototypes);

        // プロトタイプ検索フォームをモデルに渡す
        PrototypeSearchForm prototypeSearchForm = new PrototypeSearchForm();
        model.addAttribute("prototypeSearchForm", prototypeSearchForm);

        return "index";
    }
}
