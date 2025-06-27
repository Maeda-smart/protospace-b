package in.tech_camp.protospace_b.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import in.tech_camp.protospace_b.custom_user.CustomUserDetail;
import in.tech_camp.protospace_b.entity.CommentNotificationEntity;
import in.tech_camp.protospace_b.service.NotificationService;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class NotificationController {
  
  private final NotificationService notificationService;

  @GetMapping("/notifications")
  public String showNotifications(@AuthenticationPrincipal CustomUserDetail currentUser, Model model) {
    Integer userId = currentUser.getId();
    List<CommentNotificationEntity> notifications = notificationService.getCommentNotifications(userId);
    model.addAttribute("notifications", notifications);
    return "notification";
  }
}
