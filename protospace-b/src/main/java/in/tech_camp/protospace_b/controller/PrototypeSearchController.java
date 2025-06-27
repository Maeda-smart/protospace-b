package in.tech_camp.protospace_b.controller;

import java.util.List;
import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import in.tech_camp.protospace_b.custom_user.CustomUserDetail;
import in.tech_camp.protospace_b.entity.PrototypeEntity;
import in.tech_camp.protospace_b.form.PrototypeSearchForm;
import in.tech_camp.protospace_b.repository.PrototypeSearchRepository;
import in.tech_camp.protospace_b.service.PrototypeStatusService;
import lombok.AllArgsConstructor;


@Controller
@AllArgsConstructor
public class PrototypeSearchController {
  private final PrototypeSearchRepository prototypeSearchRepository;
  private final PrototypeStatusService prototypeStatusService;

  @GetMapping("/prototypes/search")
  public String searchPrototypes(@ModelAttribute("prototypeSearchForm") PrototypeSearchForm prototypeSearchForm,@AuthenticationPrincipal CustomUserDetail currentUser, Model model) {
    List<PrototypeEntity> prototypes = prototypeSearchRepository.findByPrototypeName(prototypeSearchForm.getPrototypeName());
    model.addAttribute("prototypes", prototypes);

    // ログインユーザーのID取得
    Integer userId = (currentUser != null) ? currentUser.getId() : null;

    // 検索した投稿のステータス
    Map<String, Map<Integer, ?>> prototypeStatus = prototypeStatusService.generatePrototypeStatus(prototypes, userId);
    model.addAttribute("nicePrototype", prototypeStatus.get("niceCountMap"));
    model.addAttribute("isNicePrototype", prototypeStatus.get("isNiceMap"));
    model.addAttribute("prototypeRead", prototypeStatus.get("readStatusMap"));

    model.addAttribute("prototypeSearchForm", prototypeSearchForm);

    return "prototype/search";
  }
  
}
