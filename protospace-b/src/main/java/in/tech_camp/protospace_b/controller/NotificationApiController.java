package in.tech_camp.protospace_b.controller;
 
import java.util.List;
 
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
 
import in.tech_camp.protospace_b.custom_user.CustomUserDetail;
import in.tech_camp.protospace_b.entity.CommentNotificationEntity;
import in.tech_camp.protospace_b.service.NotificationService;
import lombok.RequiredArgsConstructor;
 
@RestController
@RequiredArgsConstructor
public class NotificationApiController {
 
    private final NotificationService notificationService;
 
    @GetMapping("/api/notifications")
    public List<CommentNotificationEntity> getNotifications(@AuthenticationPrincipal CustomUserDetail currentUser) {
        Integer userId = currentUser.getId();
        return notificationService.getCommentNotifications(userId);
    }
}