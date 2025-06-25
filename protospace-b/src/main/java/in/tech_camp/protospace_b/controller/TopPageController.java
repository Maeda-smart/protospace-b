package in.tech_camp.protospace_b.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import in.tech_camp.protospace_b.custom_user.CustomUserDetail;
import in.tech_camp.protospace_b.entity.PrototypeEntity;
import in.tech_camp.protospace_b.entity.UserEntity;
import in.tech_camp.protospace_b.repository.NiceRepository;
import in.tech_camp.protospace_b.repository.PrototypeShowRepository;
import in.tech_camp.protospace_b.repository.UserDetailRepository;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class TopPageController {
  private final UserDetailRepository userDetailRepository;
  private final PrototypeShowRepository prototypeShowRepository;
  private final NiceRepository niceRepository;

  @GetMapping("")
  public String topPage(@AuthenticationPrincipal CustomUserDetail currentUser, Model model) {
    if (currentUser != null) {
      Integer userId = currentUser.getId();
      UserEntity user = userDetailRepository.findById(userId);
      model.addAttribute("user", user);
    }
    List<PrototypeEntity> prototypes = prototypeShowRepository.showAll();
    model.addAttribute("prototypes", prototypes);

    // プロトタイプごとのいいね数表示
    Map<Integer, Integer> niceCountMap = new HashMap<>();

    for (PrototypeEntity prototype : prototypes) {
      int count = niceRepository.countNiceByPrototypeId(prototype.getId());
      niceCountMap.put(prototype.getId(), count);
    }

    model.addAttribute("niceCountMap", niceCountMap);

    return "index";
  }

}
