package in.tech_camp.protospace_b.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import in.tech_camp.protospace_b.custom_user.CustomUserDetail;
import in.tech_camp.protospace_b.entity.CommentEntity;
import in.tech_camp.protospace_b.entity.PrototypeEntity;
import in.tech_camp.protospace_b.form.CommentForm;
import in.tech_camp.protospace_b.repository.CommentRepository;
import in.tech_camp.protospace_b.repository.PrototypeShowRepository;
import in.tech_camp.protospace_b.service.ReadStatusService;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class PrototypeDetailController {

    private final ReadStatusService readStatusService;

    private final PrototypeShowRepository prototypeShowRepository;
    private final CommentRepository commentRepository;

    @GetMapping("/prototypes/{prototypeId}/detail")
    public String showPrototypeDetail(@PathVariable("prototypeId") Integer prototypeId,
            @AuthenticationPrincipal CustomUserDetail currentUser, Model model) {
        Integer loginUserId = (currentUser != null) ? currentUser.getId() : null;
        // リポジトリからエンティティ取得
        PrototypeEntity prototype = prototypeShowRepository.findByPrototypeId(loginUserId, prototypeId);
        model.addAttribute("prototype", prototype);

        // コメントフォームを初期化してビューに渡す
        CommentForm commentForm = new CommentForm();
        model.addAttribute(commentForm);

        // コメント一覧を取得してビューに渡す
        List<CommentEntity> comments = commentRepository.findByPrototypeId(prototypeId);
        model.addAttribute("comments", comments);

        if (loginUserId != null) {
            // 既読
            readStatusService.markAsRead(prototypeId, loginUserId);
        }

        return "prototype/prototypeDetail";
    }
}