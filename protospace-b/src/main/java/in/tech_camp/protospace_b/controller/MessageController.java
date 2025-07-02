package in.tech_camp.protospace_b.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import in.tech_camp.protospace_b.ImageUrl;
import in.tech_camp.protospace_b.custom_user.CustomUserDetail;
import in.tech_camp.protospace_b.entity.MessageEntity;
import in.tech_camp.protospace_b.entity.RoomEntity;
import in.tech_camp.protospace_b.entity.RoomUserEntity;
import in.tech_camp.protospace_b.entity.UserEntity;
import in.tech_camp.protospace_b.form.MessageForm;
import in.tech_camp.protospace_b.repository.MessageRepository;
import in.tech_camp.protospace_b.repository.RoomRepository;
import in.tech_camp.protospace_b.repository.RoomUserRepository;
import in.tech_camp.protospace_b.repository.UserNewRepository;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class MessageController {
  private final UserNewRepository userRepository;

  private final RoomUserRepository roomUserRepository;

  private final RoomRepository roomRepository;

  private final MessageRepository messageRepository;

  private final ImageUrl imageUrl;

  @GetMapping("/rooms/{roomId}/messages")
  public String showMessages(@PathVariable("roomId") Integer roomId,@AuthenticationPrincipal CustomUserDetail currentUser, Model model){

    Integer currentUserId =currentUser.getId();

    List<RoomUserEntity> members =roomUserRepository.findByRoomId(roomId);
    boolean isMember = members.stream().anyMatch(member -> member.getUser().getId().equals(currentUserId));
    if (!isMember) {
      return "redirect:error/403";
    }

    UserEntity user = userRepository.findById(currentUser.getId());
    model.addAttribute("user", user);
    model.addAttribute("currentUser",user);
    List<RoomUserEntity> roomUserEntities = roomUserRepository.findByUserId(currentUser.getId());
    List<RoomEntity> roomList = roomUserEntities.stream()
        .map(RoomUserEntity::getRoom)
        .collect(Collectors.toList());
    model.addAttribute("rooms", roomList);

    model.addAttribute("messageForm", new MessageForm());
    
    RoomEntity room = roomRepository.findById(roomId);
    model.addAttribute("room", room);

    List<MessageEntity> messages = messageRepository.findByRoomId(roomId);
    model.addAttribute("messages", messages);
      return "chat/messages/index";
  }

  @PostMapping("/rooms/{roomId}/messages")
  public String saveMessage(@PathVariable("roomId") Integer roomId, @ModelAttribute("messageForm") MessageForm messageForm, BindingResult bindingResult, @AuthenticationPrincipal CustomUserDetail currentUser) {

    Integer currentUserId =currentUser.getId();

    List<RoomUserEntity> members =roomUserRepository.findByRoomId(roomId);
    boolean isMember = members.stream().anyMatch(member -> member.getUser().getId().equals(currentUserId));
    if (!isMember) {
      return "redirect:error/403";
    }

    messageForm.validateMessage(bindingResult);
    if (bindingResult.hasErrors()) {
      return "redirect:/rooms/" + roomId + "/messages";
    }
    
    MessageEntity message = new MessageEntity();
    message.setContent(messageForm.getContent());

    MultipartFile imageFile = messageForm.getImage();
    if (imageFile != null && !imageFile.isEmpty()) {
      try {
        String uploadDir = imageUrl.getImageUrl();
        String fileName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + "_" + imageFile.getOriginalFilename();
        Path imagePath = Paths.get(uploadDir, fileName);
        Files.copy(imageFile.getInputStream(), imagePath);
        message.setImage("/uploads/" + fileName);
      } catch (IOException e) {
        System.out.println("エラー：" + e);
        return "redirect:/rooms/" + roomId + "/messages";
      }
    }

    UserEntity user = userRepository.findById(currentUser.getId());
    RoomEntity room = roomRepository.findById(roomId);
    message.setUser(user);
    message.setRoom(room);

    try {
      messageRepository.insert(message);
    } catch (Exception e) {
      System.out.println("エラー：" + e);
    }

    return "redirect:/rooms/" + roomId + "/messages";
  }
}
