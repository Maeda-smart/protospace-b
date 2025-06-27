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
import in.tech_camp.protospace_b.repository.NiceRepository;
import in.tech_camp.protospace_b.repository.UserDetailRepository;
import in.tech_camp.protospace_b.service.ReadStatusService;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class RankingController {

  private final NiceRepository niceRepository;
  private final UserDetailRepository userDetailRepository;
  private final ReadStatusService readStatusService;
  
  // ランキングページに遷移
  @GetMapping("/prototypes/ranking")
  public String showRanking(@AuthenticationPrincipal CustomUserDetail currentUser, Model model) {

    // 既読・未読を管理
    if (currentUser != null) {
      Integer userId = currentUser.getId();
      List<ReadStatusEntity> readList = readStatusService.findAllByUserId(userId);
      Map<Integer, Boolean> readStatusMap = readList.stream()
        .collect(Collectors.toMap(
          ReadStatusEntity::getPrototypeId,
          r -> true
        ));
      model.addAttribute("readStatusMap", readStatusMap);
    }

    // いいね数順に並び替えたプロトタイプを取得
    List<PrototypeEntity> rankingPrototypes = niceRepository.findPrototypesOrderByCountDesc();
    model.addAttribute("rankingPrototypes", rankingPrototypes);

     // プロトタイプごとのいいね数表示
    Map<Integer, Integer> niceCountMap = new HashMap<>();

    for (PrototypeEntity rankingPrototype : rankingPrototypes) {
      int count = niceRepository.countNiceByPrototypeId(rankingPrototype.getId());
      niceCountMap.put(rankingPrototype.getId(), count);
    }
    model.addAttribute("niceCountMap", niceCountMap);

    // ログインユーザーが各プロトタイプに対し、いいねしたかを判定
    Map<Integer, Boolean> isNiceMap = new HashMap<>();
    if(currentUser != null) {
    Integer userId = currentUser.getId();
    
      for (PrototypeEntity rankingPrototype : rankingPrototypes) {
        boolean isNice = niceRepository.existNice(rankingPrototype.getId(), userId);
        isNiceMap.put(rankingPrototype.getId(), isNice);
      }
    } else {
    // ログインしていない場合はすべてfalseに設定
    for (PrototypeEntity rankingPrototype : rankingPrototypes) {
        isNiceMap.put(rankingPrototype.getId(), false);
    }
    }
    model.addAttribute("isNiceMap", isNiceMap);

    return "prototype/ranking";
  }
}
