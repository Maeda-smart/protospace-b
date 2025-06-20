package in.tech_camp.protospace_b.controller;

import org.hibernate.validator.internal.engine.groups.ValidationOrder;
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
import in.tech_camp.protospace_b.repository.PrototypeDetailRepository;
import in.tech_camp.protospace_b.repository.UserDetailRepository;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class CommentController {

  private final CommentRepository commentRepository;

  private final PrototypeDetailRepository prototypeDetailRepository;

  private final UserDetailRepository userDetailRepository;

  @PostMapping("/prototype/{prototypeId}/comment")
  public String createComment(@PathVariable("prototypeId") Integer prototypeId, 
                            @ModelAttribute("commentForm") @Validated(ValidationOrder.class) CommentForm commentForm,
                            BindingResult result,
                            @AuthenticationPrincipal CustomUserDetail currentUser, Model model) {

    PrototypeEntity prototype = prototypeDetailRepository.findByPrototypeId(prototypeId);

    if (result.hasErrors()) {
        model.addAttribute("errorMessages", result.getAllErrors());
        model.addAttribute("prototype", prototype);
        model.addAttribute("commentForm", commentForm);
        return "tweets/detail";
    }

    CommentEntity comment = new CommentEntity();
    comment.setText(commentForm.getText());
    comment.setPrototype(prototype);
    comment.setUser(userDetailRepository.findById(currentUser.getId()));

    try {
      commentRepository.insert(comment);
    } catch (Exception e) {
      model.addAttribute("prototype", prototype);
      model.addAttribute("commentForm", commentForm);
      System.out.println("エラー：" + e);
      return "prototype/prototypeDetail";
    }

    return "redirect:/prototype/" + prototypeId;
  }
}