package in.tech_camp.protospace_b.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import in.tech_camp.protospace_b.custom_user.CustomUserDetail;
import in.tech_camp.protospace_b.entity.CommentEntity;
import in.tech_camp.protospace_b.entity.PinEntity;
import in.tech_camp.protospace_b.entity.PrototypeEntity;
import in.tech_camp.protospace_b.form.CommentForm;
import in.tech_camp.protospace_b.repository.BookmarkRepository;
import in.tech_camp.protospace_b.repository.CommentRepository;
import in.tech_camp.protospace_b.repository.NiceRepository;
import in.tech_camp.protospace_b.repository.PinRepository;
import in.tech_camp.protospace_b.repository.PrototypeShowRepository;
import in.tech_camp.protospace_b.service.ReadStatusService;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class PrototypeDetailController {

    private final ReadStatusService readStatusService;

    private final PrototypeShowRepository prototypeShowRepository;
    private final CommentRepository commentRepository;
    private final BookmarkRepository bookmarkRepository;
    private final NiceRepository niceRepository;
    private final PinRepository pinRepository;

    @GetMapping("/prototypes/{prototypeId}/detail")
    public String showPrototypeDetail(@PathVariable("prototypeId") Integer prototypeId,
            @AuthenticationPrincipal CustomUserDetail currentUser, Model model) {
        // リポジトリからエンティティ取得
        PrototypeEntity prototype = prototypeShowRepository.findByPrototypeId(prototypeId);

        if (currentUser != null) {
            Integer ownerUser = currentUser.getId();
            List<PinEntity> pinEntity = pinRepository.findPinByUserId(ownerUser);
            boolean isPinned = pinRepository.count(ownerUser, prototypeId) > 0;
            model.addAttribute("isPinned", isPinned);
        } else {
            model.addAttribute("isPinned", false);
        }

        model.addAttribute("prototype", prototype);

        // コメントフォームを初期化してビューに渡す
        CommentForm commentForm = new CommentForm();
        model.addAttribute(commentForm);

        // コメント一覧を取得してビューに渡す
        List<CommentEntity> comments = commentRepository.findByPrototypeId(prototypeId);
        model.addAttribute("comments", comments);

        // プロトタイプごとのいいね数を取得してビューに渡す
        int countNice = niceRepository.countNiceByPrototypeId(prototypeId);
        model.addAttribute("countNice", countNice);

        if (currentUser != null) {
        Integer userId = currentUser.getId();
        model.addAttribute("userId", userId);
        boolean isBookmarked = bookmarkRepository.existBookmark(prototypeId, userId);
        model.addAttribute("isBookmarked", isBookmarked);

        // いいね済みの状態を表示
        boolean isNice = niceRepository.existNice(prototypeId, userId);
        model.addAttribute("isNice", isNice);

        // 既読
        readStatusService.markAsRead(prototypeId, userId);
        }

        return "prototype/prototypeDetail";
    }
}