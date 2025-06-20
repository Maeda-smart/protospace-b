package in.tech_camp.protospace_b.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import in.tech_camp.protospace_b.entity.PrototypeEntity;
import in.tech_camp.protospace_b.form.CommentForm;
import in.tech_camp.protospace_b.repository.PrototypeDetailRepository;

@Controller
public class PrototypeDetailController {

    private final PrototypeDetailRepository prototypeDetailRepository;

    // コンストラクタインジェクション（Spring Boot 4.x以降は@Autowried不要！）
    public PrototypeDetailController(PrototypeDetailRepository prototypeDetailRepository) {
        this.prototypeDetailRepository = prototypeDetailRepository;
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

        // return "protoType/prototypeDetail";
        return "/prototype/prototypeDetail";
    }
}