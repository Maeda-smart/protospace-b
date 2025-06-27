package in.tech_camp.protospace_b.controller;

import java.util.List;
import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import in.tech_camp.protospace_b.custom_user.CustomUserDetail;
import in.tech_camp.protospace_b.entity.PrototypeEntity;
import in.tech_camp.protospace_b.repository.NiceRepository;
import in.tech_camp.protospace_b.service.PrototypeStatusService;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class RankingController {

  private final NiceRepository niceRepository;
  private final PrototypeStatusService prototypeStatusService;
  
  // ランキングページに遷移
  @GetMapping("/prototypes/ranking")
  public String showRanking(@AuthenticationPrincipal CustomUserDetail currentUser, Model model) {

    // ログインユーザーのID取得
    Integer userId = (currentUser != null) ? currentUser.getId() : null;

    // いいね数順に並び替えたプロトタイプを取得
    List<PrototypeEntity> rankingPrototypes = niceRepository.findPrototypesOrderByCountDesc();
    model.addAttribute("rankingPrototypes", rankingPrototypes);

    // プロトタイプのステータス
    Map<String, Map<Integer, ?>> prototypeStatus = prototypeStatusService.generatePrototypeStatus(rankingPrototypes, userId);

    model.addAttribute("nicePrototype", prototypeStatus.get("niceCountMap"));
    model.addAttribute("isNicePrototype", prototypeStatus.get("isNiceMap"));
    model.addAttribute("prototypeRead", prototypeStatus.get("readStatusMap"));

    return "prototype/ranking";
  }
}
