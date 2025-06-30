package in.tech_camp.protospace_b.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import in.tech_camp.protospace_b.custom_user.CustomUserDetail;
import in.tech_camp.protospace_b.entity.CommentEntity;
import in.tech_camp.protospace_b.entity.PrototypeEntity;
import in.tech_camp.protospace_b.form.CommentForm;
import in.tech_camp.protospace_b.repository.CommentRepository;
import in.tech_camp.protospace_b.repository.PrototypeShowRepository;
import in.tech_camp.protospace_b.repository.UserDetailRepository;
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
                            @ModelAttribute("commentForm") CommentForm commentForm,
                            @AuthenticationPrincipal CustomUserDetail currentUser, Model model) {

    // プロトタイプ取得
    PrototypeEntity prototype = prototypeShowRepository.findByPrototypeId(prototypeId);
    
    // コメントが空の場合はリダイレクト
    if (commentForm.getText() == null || commentForm.getText().trim().isEmpty()) {
        return "redirect:/prototypes/" + prototypeId + "/detail";
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
      return "prototype/prototypeDetail";
    }

    return "redirect:/prototypes/" + prototypeId + "/detail";
  }

  // コメント削除機能
    @PostMapping("/prototypes/{prototypeId}/comment/delete")
    public String deleteComment(@PathVariable("prototypeId") Integer prototypeId, @RequestParam("commentId")Integer commentId, 
                                @AuthenticationPrincipal CustomUserDetail currentUser, Model model) {

      try {
        commentRepository.deleteById(commentId);
      } catch (Exception e) {
        System.out.println("Error: " + e);
      }
      return "redirect:/prototypes/" + prototypeId + "/detail";
    }
}