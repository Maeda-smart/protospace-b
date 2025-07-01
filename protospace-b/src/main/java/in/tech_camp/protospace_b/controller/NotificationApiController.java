package in.tech_camp.protospace_b.controller;
 
import java.util.List;
 
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
 
import in.tech_camp.protospace_b.custom_user.CustomUserDetail;
import in.tech_camp.protospace_b.entity.CommentNotificationEntity;
import in.tech_camp.protospace_b.service.NotificationService;
import lombok.RequiredArgsConstructor;

 
@RestController
@RequiredArgsConstructor
public class NotificationApiController {
 
    private final NotificationService notificationService;
 
    // 未読通知一覧取得
    @GetMapping("/api/notifications")
    public List<CommentNotificationEntity> getNotifications(@AuthenticationPrincipal CustomUserDetail currentUser) {
        Integer userId = currentUser.getId();
        return notificationService.getUnreadNotifications(userId);
    }

    // 通知を既読にする
    @PatchMapping("/api/notifications/{notificationId}/read")
    public ResponseEntity<Void> markNotificationAsRead(@PathVariable Integer notificationId) {
        try {
            System.out.println("PATCH/api/notifications/" + notificationId + "/read called");
            notificationService.markAsRead(notificationId);
            System.out.println("Notification" + notificationId + "mark as read.");
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
    
}