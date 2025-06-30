package in.tech_camp.protospace_b.service;

import java.util.List;

import org.springframework.stereotype.Service;

import in.tech_camp.protospace_b.entity.CommentNotificationEntity;
import in.tech_camp.protospace_b.repository.CommentNotificationRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService {
  private final CommentNotificationRepository commentNotificationRepository;

  public List<CommentNotificationEntity> getCommentNotifications(Integer userId) {
    return commentNotificationRepository.findCommentNotificationByUserId(userId);
  }
}
