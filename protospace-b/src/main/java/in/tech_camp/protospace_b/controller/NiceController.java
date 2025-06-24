package in.tech_camp.protospace_b.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import in.tech_camp.protospace_b.custom_user.CustomUserDetail;
import in.tech_camp.protospace_b.entity.NiceEntity;
import in.tech_camp.protospace_b.entity.PrototypeEntity;
import in.tech_camp.protospace_b.entity.UserEntity;
import in.tech_camp.protospace_b.repository.NiceRepository;
import in.tech_camp.protospace_b.repository.PrototypeDetailRepository;
import in.tech_camp.protospace_b.repository.UserNewRepository;
import lombok.AllArgsConstructor;


@Controller
@AllArgsConstructor
public class NiceController {
  
  private final NiceRepository niceRepository;

  private final UserNewRepository userNewRepository;

  private final PrototypeDetailRepository prototypeDetailRepository;
  
  // いいね送信メソッド
  @PostMapping("/prototypes/{prototypeId}/nice")
  public String nicePrototype(@PathVariable("prototypeId") Integer prototypeId,
    @AuthenticationPrincipal CustomUserDetail currentUser) {

    Integer userId = currentUser.getId();

    // いいね済みかを判別
    boolean isNice = niceRepository.existNice(prototypeId, userId);
      
    if(isNice) {
      // いいね済みなら削除
      niceRepository.deleteNice(prototypeId, userId);
    } else {
    // プロトタイプとログインしているユーザー情報を取得
    PrototypeEntity prototype = prototypeDetailRepository.findByPrototypeId(prototypeId);
    UserEntity user = userNewRepository.findById(userId);
    
    NiceEntity nice = new NiceEntity();
    nice.setPrototype(prototype);
    nice.setUser(user);

    // いいねを追加
    niceRepository.insert(nice);
    }
      
    return "redirect:/prototypes/" + prototypeId + "/detail";
  }
  
}
