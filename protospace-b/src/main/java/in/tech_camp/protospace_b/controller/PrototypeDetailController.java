package in.tech_camp.protospace_b.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import in.tech_camp.protospace_b.entity.CommentEntity;
import in.tech_camp.protospace_b.entity.PinEntity;
import in.tech_camp.protospace_b.entity.PrototypeEntity;
import in.tech_camp.protospace_b.form.CommentForm;
import in.tech_camp.protospace_b.repository.CommentRepository;
import in.tech_camp.protospace_b.repository.PrototypeDetailRepository;
import in.tech_camp.protospace_b.custom_user.CustomUserDetail;
import in.tech_camp.protospace_b.repository.PinRepository;

@Controller

public class PrototypeDetailController {

    private final PrototypeDetailRepository prototypeDetailRepository;

    private final CommentRepository commentRepository;

    private final PinRepository pinRepository;

    // コンストラクタインジェクション（Spring Boot 4.x以降は@Autowried不要！）
    public PrototypeDetailController(PrototypeDetailRepository prototypeDetailRepository,CommentRepository commentRepository, PinRepository pinRepository) {
        this.prototypeDetailRepository = prototypeDetailRepository;
        this.commentRepository = commentRepository;
        this.pinRepository = pinRepository;
    }

    @GetMapping("/prototypes/{prototypeId}/detail")
    public String showPrototypeDetail(@PathVariable("prototypeId") Integer prototypeId, Model model,
                                        @AuthenticationPrincipal CustomUserDetail currentUser
                                        ) {
        // リポジトリからエンティティ取得
        PrototypeEntity prototype = prototypeDetailRepository.findByPrototypeId(prototypeId);

        if (currentUser != null) {
            Integer ownerUser = currentUser.getId();
            List<PinEntity> pinEntity = pinRepository.findPinByUserId(ownerUser);
            boolean isPinned = pinRepository.count(ownerUser, prototypeId) > 0;
            model.addAttribute("isPinned", isPinned);
            System.out.println(pinEntity);
        } else {
            model.addAttribute("isPinned", false);
        }

        model.addAttribute("prototype", prototype);
        model.addAttribute("prototypeId", prototypeId);

        // コメントフォームを初期化してビューに渡す
        CommentForm commentForm = new CommentForm();
        model.addAttribute(commentForm);

        // コメント一覧を取得してビューに渡す
        List<CommentEntity> comments = commentRepository.findByPrototypeId(prototypeId);
        model.addAttribute("comments", comments);

        return "prototype/prototypeDetail";
    }
}