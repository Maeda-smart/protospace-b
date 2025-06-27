package in.tech_camp.protospace_b.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import in.tech_camp.protospace_b.custom_user.CustomUserDetail;
import in.tech_camp.protospace_b.entity.CommentEntity;
import in.tech_camp.protospace_b.entity.PrototypeEntity;
import in.tech_camp.protospace_b.form.CommentForm;
import in.tech_camp.protospace_b.repository.CommentRepository;
import in.tech_camp.protospace_b.repository.PrototypeShowRepository;
import in.tech_camp.protospace_b.repository.UserDetailRepository;
import in.tech_camp.protospace_b.validation.ValidationOrder;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class CommentController {

  private final CommentRepository commentRepository;

  private final PrototypeShowRepository prototypeShowRepository;

  private final UserDetailRepository userDetailRepository;

  // コメント保存機能
  @PostMapping("/prototype/{prototypeId}/comment")
  public String createComment(@PathVariable("prototypeId") Integer prototypeId, 
                            @ModelAttribute("commentForm") @Validated(ValidationOrder.class) CommentForm commentForm,
                            BindingResult result,
                            @AuthenticationPrincipal CustomUserDetail currentUser, Model model) {

    // プロトタイプ取得
    PrototypeEntity prototype = prototypeShowRepository.findByPrototypeId(prototypeId);
    // コメント取得
    List<CommentEntity> comments = commentRepository.findByPrototypeId(prototypeId);

    // バリデーション
    if (result.hasErrors()) {
        model.addAttribute("errorMessages", result.getAllErrors());
        model.addAttribute("prototype", prototype);
        model.addAttribute("commentForm", commentForm);
        model.addAttribute("comments", comments);
        return "prototype/prototypeDetail";
    }

    // コメント情報をセット
    CommentEntity comment = new CommentEntity();
    comment.setText(commentForm.getText());
    comment.setPrototype(prototype);
    comment.setUser(userDetailRepository.findById(currentUser.getId()));
    // System.out.println(comment);

    try {
      commentRepository.insert(comment);
    } catch (Exception e) {
      model.addAttribute("prototype", prototype);
      model.addAttribute("commentForm", commentForm);
      System.out.println("エラー：" + e);
      return "prototypes/prototypeDetail";
    }

    return "redirect:/prototypes/" + prototypeId + "/detail";
  }
}