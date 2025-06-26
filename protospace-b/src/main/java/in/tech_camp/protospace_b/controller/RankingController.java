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
import in.tech_camp.protospace_b.repository.NiceRepository;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class RankingController {

  private final NiceRepository niceRepository;
  
  // ランキングページに遷移
  @GetMapping("/prototypes/ranking")
  public String showRanking(@AuthenticationPrincipal CustomUserDetail currentUser, Model model) {

    // いいね数順に並び替えたプロトタイプを取得
    List<PrototypeEntity> rankingPrototypes = niceRepository.findPrototypesOrderByCountDesc();
    model.addAttribute("rankingPrototypes", rankingPrototypes);

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
