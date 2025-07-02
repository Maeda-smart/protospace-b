package in.tech_camp.protospace_b.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import in.tech_camp.protospace_b.custom_user.CustomUserDetail;
import in.tech_camp.protospace_b.entity.PrototypeEntity;
import in.tech_camp.protospace_b.repository.PrototypeDeleteRepository;
import in.tech_camp.protospace_b.repository.PrototypeShowRepository;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class DeleteController {

    private final PrototypeDeleteRepository prototypeDeleteRepository;
    private final PrototypeShowRepository prototypeShowRepository;

    @PostMapping("/prototypes/{prototypeId}/delete")
    public String deletePrototype(
            @PathVariable("prototypeId") Integer prototypeId,
            @AuthenticationPrincipal CustomUserDetail currentUser) {

        PrototypeEntity prototypeEntity = prototypeShowRepository.findByPrototypeId(currentUser.getId(), prototypeId);
        Integer ownerUserId = prototypeEntity.getUser().getId();

        // 投稿主・モデレーター・管理者以外は拒否
        boolean isOwner = ownerUserId.equals(currentUser.getId());
        boolean isModeratorOrAdmin = 
                "ROLE_MODERATOR".equals(currentUser.getUser().getRoleName()) ||
                "ROLE_ADMIN".equals(currentUser.getUser().getRoleName());

        if (!isOwner && !isModeratorOrAdmin) {
            return "redirect:/";
        }

        try {
            prototypeDeleteRepository.deleteById(prototypeId);
        } catch (Exception e) {
            System.out.println("エラー：" + e);
            return "redirect:/";
        }
        return "redirect:/";
    }
}
