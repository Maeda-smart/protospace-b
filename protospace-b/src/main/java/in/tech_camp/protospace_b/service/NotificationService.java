package in.tech_camp.protospace_b.service;
 
import java.util.List;

import org.springframework.stereotype.Service;

import in.tech_camp.protospace_b.entity.CommentNotificationEntity;
import in.tech_camp.protospace_b.repository.CommentNotificationRepository;
import lombok.RequiredArgsConstructor;
 
@Service
@RequiredArgsConstructor
public class NotificationService {
 
  private final CommentNotificationRepository notificationRepository;
 
  public List<CommentNotificationEntity> getUnreadNotifications(Integer userId) {
    return notificationRepository.findUnreadByRecipientId(userId);
  }
 
  public void markAsRead(Integer notificationId) {
    System.out.println("markAsRead called with notificationId = " + notificationId);
    notificationRepository.markAsRead(notificationId);
    System.out.println("update executed for notificationId = " + notificationId);
  }
 
  public void createNotification(CommentNotificationEntity notification) {
    notificationRepository.insertNotification(notification);
  }
}