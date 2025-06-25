package in.tech_camp.protospace_b.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import in.tech_camp.protospace_b.custom_user.CustomUserDetail;
import in.tech_camp.protospace_b.entity.PrototypeEntity;
import in.tech_camp.protospace_b.entity.UserEntity;
import in.tech_camp.protospace_b.form.PrototypeSearchForm;
import in.tech_camp.protospace_b.repository.PrototypeShowRepository;
import in.tech_camp.protospace_b.repository.UserDetailRepository;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class TopPageController {
  private final UserDetailRepository userDetailRepository;
  private final PrototypeShowRepository prototypeShowRepository;

  @GetMapping("")
  public String topPage(@AuthenticationPrincipal CustomUserDetail currentUser, Model model) {
    if (currentUser != null) {
      Integer userId = currentUser.getId();
      UserEntity user = userDetailRepository.findById(userId);
      model.addAttribute("user", user);
    }
    List<PrototypeEntity> prototypes = prototypeShowRepository.showAll();
    PrototypeSearchForm prototypeSearchForm = new PrototypeSearchForm();
    model.addAttribute("prototypes", prototypes);
    model.addAttribute("prototypeSearchForm", prototypeSearchForm);

    return "index";
  }

}
