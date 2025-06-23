package in.tech_camp.protospace_b.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import in.tech_camp.protospace_b.entity.CommentEntity;
import in.tech_camp.protospace_b.entity.PrototypeEntity;
import in.tech_camp.protospace_b.form.CommentForm;
import in.tech_camp.protospace_b.repository.CommentRepository;
import in.tech_camp.protospace_b.repository.PrototypeDetailRepository;

@Controller
public class PrototypeDetailController {

    private final PrototypeDetailRepository prototypeDetailRepository;

    private final CommentRepository commentRepository;

    // コンストラクタインジェクション（Spring Boot 4.x以降は@Autowried不要！）
    public PrototypeDetailController(PrototypeDetailRepository prototypeDetailRepository,CommentRepository commentRepository) {
        this.prototypeDetailRepository = prototypeDetailRepository;
        this.commentRepository = commentRepository;
    }

    @GetMapping("/prototypes/{prototypeId}/detail")
    public String editPrototype(@PathVariable("prototypeId") Integer prototypeId, Model model) {
        // リポジトリからエンティティ取得
        PrototypeEntity prototype = prototypeDetailRepository.findByPrototypeId(prototypeId);

        // prototypeForm.setId(prototype.getId());

        // System.out.println(prototype);

        model.addAttribute("prototype", prototype);
        model.addAttribute("prototypeId", prototypeId);

        // コメントフォームを初期化してビューに渡す
        CommentForm commentForm = new CommentForm();
        model.addAttribute(commentForm);

        // コメント一覧を取得してビューに渡す
        List<CommentEntity> comments = commentRepository.findByPrototypeId(prototypeId);
        model.addAttribute("comments", comments);

        // return "protoType/prototypeDetail";
        return "prototype/prototypeDetail";
    }
}