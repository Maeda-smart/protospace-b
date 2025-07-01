package in.tech_camp.protospace_b.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import in.tech_camp.protospace_b.custom_user.CustomUserDetail;
import in.tech_camp.protospace_b.entity.NiceEntity;
import in.tech_camp.protospace_b.entity.PrototypeEntity;
import in.tech_camp.protospace_b.entity.UserEntity;
import in.tech_camp.protospace_b.repository.NiceRepository;
import in.tech_camp.protospace_b.repository.PrototypeShowRepository;
import in.tech_camp.protospace_b.repository.UserNewRepository;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class NiceController {

    private final NiceRepository niceRepository;

    private final PrototypeShowRepository prototypeShowRepository;

    private final UserNewRepository userNewRepository;

    // いいねを送るメソッド
    @PostMapping("/prototypes/{prototypeId}/nice")
    public String sendNice(@PathVariable("prototypeId") Integer prototypeId,
            @AuthenticationPrincipal CustomUserDetail currentUser, Model model) {

        // プロトタイプとログインしているユーザー情報を取得
        Integer userId = currentUser.getId();
        PrototypeEntity prototype = prototypeShowRepository.findByPrototypeId(currentUser.getId(), prototypeId);
        UserEntity user = userNewRepository.findById(userId);

        // いいね済みかを判別
        boolean isNice = niceRepository.existNice(prototypeId, userId);

        if (isNice) {
            // いいね済みなら削除
            niceRepository.deleteNice(prototypeId, userId);
        } else {
            // NiceEntityにセット
            NiceEntity nice = new NiceEntity();
            nice.setPrototype(prototype);
            nice.setUser(user);
            niceRepository.insert(nice);
        }

        return "redirect:/prototypes/" + prototypeId + "/detail";
    }

}
