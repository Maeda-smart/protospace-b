package in.tech_camp.protospace_b.controller;

import java.util.HashMap;
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
import in.tech_camp.protospace_b.repository.NiceRepository;
import in.tech_camp.protospace_b.repository.PrototypeSearchRepository;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class PrototypeSearchController {
  private final PrototypeSearchRepository prototypeSearchRepository;
  private final NiceRepository niceRepository;

  @GetMapping("/prototypes/search")
  public String searchPrototypes(@ModelAttribute("prototypeSearchForm") PrototypeSearchForm prototypeSearchForm,
      @AuthenticationPrincipal CustomUserDetail currentUser, Model model) {
    List<PrototypeEntity> prototypes = prototypeSearchRepository
        .findByPrototypeName(prototypeSearchForm.getPrototypeName());

    // プロトタイプごとのいいね数表示
    Map<Integer, Integer> niceCountMap = new HashMap<>();

    for (PrototypeEntity prototype : prototypes) {
      int count = niceRepository.countNiceByPrototypeId(prototype.getId());
      niceCountMap.put(prototype.getId(), count);
    }
    model.addAttribute("niceCountMap", niceCountMap);

    // ログインユーザーが各プロトタイプに対し、いいねしたかを判定
    Map<Integer, Boolean> isNiceMap = new HashMap<>();
    if (currentUser != null) {
      Integer userId = currentUser.getId();

      for (PrototypeEntity prototype : prototypes) {
        boolean isNice = niceRepository.existNice(prototype.getId(), userId);
        isNiceMap.put(prototype.getId(), isNice);
      }
    } else {
      // ログインしていない場合はすべてfalseに設定
      for (PrototypeEntity prototype : prototypes) {
        isNiceMap.put(prototype.getId(), false);
      }
    }
    model.addAttribute("isNiceMap", isNiceMap);
    model.addAttribute("prototypes", prototypes);
    model.addAttribute("prototypeSearchForm", prototypeSearchForm);
    return "prototype/search";
  }

}
