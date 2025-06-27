package in.tech_camp.protospace_b.controller;

import java.util.List;
import java.util.Map;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import in.tech_camp.protospace_b.custom_user.CustomUserDetail;
import in.tech_camp.protospace_b.entity.PrototypeEntity;
import in.tech_camp.protospace_b.form.PrototypeSearchForm;
import in.tech_camp.protospace_b.repository.PrototypeShowRepository;
import in.tech_camp.protospace_b.service.PrototypeStatusService;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class TopPageController {

  private final PrototypeShowRepository prototypeShowRepository;
  private final PrototypeStatusService prototypeStatusService;

  @GetMapping("")
  public String topPage(@AuthenticationPrincipal CustomUserDetail currentUser, Model model) {
    
    // ログイン時のみuserIdを取得
    Integer userId = (currentUser != null) ? currentUser.getId() : null;
   
    // 全プロトタイプ取得を取得し、モデルに渡す
    List<PrototypeEntity> prototypes = prototypeShowRepository.showAll();
    model.addAttribute("prototypes", prototypes);

    // プロトタイプ検索フォームをモデルに渡す
    PrototypeSearchForm prototypeSearchForm = new PrototypeSearchForm();
    model.addAttribute("prototypeSearchForm", prototypeSearchForm);


    // プロトタイプのステータス
    Map<String, Map<Integer, ?>> status = prototypeStatusService.generatePrototypeStatus(prototypes, userId);

    model.addAttribute("niceCountMap", status.get("niceCountMap"));
    model.addAttribute("isNiceMap", status.get("isNiceMap"));
    model.addAttribute("readStatusMap", status.get("readStatusMap"));

    return "index";

  }
}
