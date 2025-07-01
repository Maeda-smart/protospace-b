package in.tech_camp.protospace_b.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import in.tech_camp.protospace_b.custom_user.CustomUserDetail;
import in.tech_camp.protospace_b.entity.PrototypeEntity;
import in.tech_camp.protospace_b.form.PrototypeSearchForm;
import in.tech_camp.protospace_b.repository.PrototypeShowRepository;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class PrototypeSearchController {
  private final PrototypeShowRepository prototypeShowRepository;

  @GetMapping("/prototypes/search")
  public String searchPrototypes(@ModelAttribute("prototypeSearchForm") PrototypeSearchForm prototypeSearchForm,
      @AuthenticationPrincipal CustomUserDetail currentUser, Model model) {
    // ログインユーザーのID取得
    Integer userId = (currentUser != null) ? currentUser.getId() : null;
    List<PrototypeEntity> prototypes = prototypeShowRepository.findByPrototypeName(userId,
        prototypeSearchForm.getPrototypeName());
    model.addAttribute("prototypes", prototypes);

    model.addAttribute("prototypeSearchForm", prototypeSearchForm);

    return "prototype/search";
  }

}
