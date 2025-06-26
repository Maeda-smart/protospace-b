package in.tech_camp.protospace_b.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import in.tech_camp.protospace_b.custom_user.CustomUserDetail;
import in.tech_camp.protospace_b.entity.PrototypeEntity;
import in.tech_camp.protospace_b.entity.ReadStatusEntity;
import in.tech_camp.protospace_b.entity.UserEntity;
import in.tech_camp.protospace_b.form.PrototypeSearchForm;
import in.tech_camp.protospace_b.repository.NiceRepository;
import in.tech_camp.protospace_b.repository.PrototypeShowRepository;
import in.tech_camp.protospace_b.repository.UserDetailRepository;
import in.tech_camp.protospace_b.service.ReadStatusService;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class TopPageController {
  private final UserDetailRepository userDetailRepository;
  private final PrototypeShowRepository prototypeShowRepository;
  private final ReadStatusService readStatusService;

  private final NiceRepository niceRepository;

  @GetMapping("")
  public String topPage(@AuthenticationPrincipal CustomUserDetail currentUser, Model model) {
    if (currentUser != null) {
      Integer userId = currentUser.getId();
      UserEntity user = userDetailRepository.findById(userId);
      model.addAttribute("user", user);

      List<ReadStatusEntity> readList = readStatusService.findAllByUserId(userId);
      Map<Integer, Boolean> readStatusMap = readList.stream()
        .collect(Collectors.toMap(
          ReadStatusEntity::getPrototypeId,
          r -> true
        ));
      model.addAttribute("readStatusMap", readStatusMap);
    }
    List<PrototypeEntity> prototypes = prototypeShowRepository.showAll();
    PrototypeSearchForm prototypeSearchForm = new PrototypeSearchForm();
    model.addAttribute("prototypes", prototypes);

    
    // プロトタイプごとのいいね数表示
    Map<Integer, Integer> niceCountMap = new HashMap<>();

    for (PrototypeEntity prototype : prototypes) {
      int count = niceRepository.countNiceByPrototypeId(prototype.getId());
      niceCountMap.put(prototype.getId(), count);
    }
    model.addAttribute("niceCountMap", niceCountMap);

    // ログインユーザーが各プロトタイプに対し、いいねしたかを判定
    Map<Integer, Boolean> isNiceMap = new HashMap<>();
    if(currentUser != null) {
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

    model.addAttribute("prototypeSearchForm", prototypeSearchForm);

    return "index";

  }
}
