package in.tech_camp.protospace_b.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import in.tech_camp.protospace_b.custom_user.CustomUserDetail;
import in.tech_camp.protospace_b.entity.RoomEntity;
import in.tech_camp.protospace_b.entity.RoomUserEntity;
import in.tech_camp.protospace_b.entity.UserEntity;
import in.tech_camp.protospace_b.form.RoomForm;
import in.tech_camp.protospace_b.repository.RoomRepository;
import in.tech_camp.protospace_b.repository.RoomUserRepository;
import in.tech_camp.protospace_b.repository.UserNewRepository;
import in.tech_camp.protospace_b.validation.ValidationOrder;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class RoomController {
  private final RoomUserRepository roomUserRepository;

  private final RoomRepository roomRepository;

  private final UserNewRepository userRepository;
  
  @PostMapping("/dm/{userId}")
  public String startDirectMessage(@PathVariable("userId") Integer targetUserId,
                                 @AuthenticationPrincipal CustomUserDetail currentUser) {
    Integer currentUserId = currentUser.getId();
 
    // 自分自身とはチャット不可
    if (currentUserId.equals(targetUserId)) {
        return "redirect:/users/" + targetUserId;
    }
 
    // 既存の2人だけのチャットルームを探す
    List<RoomUserEntity> myRooms = roomUserRepository.findByUserId(currentUserId);
    for (RoomUserEntity roomUser : myRooms) {
        RoomEntity room = roomUser.getRoom();
        List<RoomUserEntity> members = roomUserRepository.findByRoomId(room.getId());
        if (members.size() == 2 &&
            members.stream().anyMatch(u -> u.getUser().getId().equals(targetUserId))) {
            return "redirect:/rooms/" + room.getId() + "/messages";
        }
    }

    UserEntity targetUser = userRepository.findById(targetUserId);
    String targetUserName = targetUser.getNickname();
 
    // 新しいDMルームを作成
    RoomEntity newRoom = new RoomEntity();
    newRoom.setName(targetUserName + "さんとのチャット");
    roomRepository.insert(newRoom);
 
    // 両ユーザーを登録
    RoomUserEntity currentRoomUser = new RoomUserEntity();
    currentRoomUser.setRoom(newRoom);
    currentRoomUser.setUser(userRepository.findById(currentUserId));
    roomUserRepository.insert(currentRoomUser);
 
    RoomUserEntity targetRoomUser = new RoomUserEntity();
    targetRoomUser.setRoom(newRoom);
    targetRoomUser.setUser(userRepository.findById(targetUserId));
    roomUserRepository.insert(targetRoomUser);
 
    return "redirect:/rooms/" + newRoom.getId() + "/messages";
}
  
  @GetMapping("/rooms/new")
  public String showRoomNew(@AuthenticationPrincipal CustomUserDetail currentUser, Model model){
    List<UserEntity> users = userRepository.findAllExcept(currentUser.getId());
    model.addAttribute("users", users);
    model.addAttribute("roomForm", new RoomForm());
      return "chat/rooms/new";
  }

  @GetMapping("/rooms")
    public String listRooms(@AuthenticationPrincipal CustomUserDetail currentUser, Model model) {
    Integer currentUserId = currentUser.getId();
    List<RoomUserEntity> roomUsers = roomUserRepository.findByUserId(currentUserId);
 
    List<RoomEntity> rooms = roomUsers.stream()
        .map(RoomUserEntity::getRoom)
        .collect(Collectors.toList());
 
    model.addAttribute("rooms", rooms);
    return "chat/toppage";
}

  @PostMapping("/rooms")
  public String createRoom(@ModelAttribute("RoomForm") @Validated(ValidationOrder.class) RoomForm roomForm, BindingResult bindingResult, @AuthenticationPrincipal CustomUserDetail currentUser, Model model){
    if (bindingResult.hasErrors()) {
      List<String> errorMessages = bindingResult.getAllErrors().stream()
                              .map(DefaultMessageSourceResolvable::getDefaultMessage)
                              .collect(Collectors.toList());
      List<UserEntity> users = userRepository.findAllExcept(currentUser.getId());
      model.addAttribute("users", users);
      model.addAttribute("roomForm", roomForm);
      model.addAttribute("errorMessages", errorMessages);
      return "chat/rooms/new";
    }

    RoomEntity roomEntity = new RoomEntity();
    roomEntity.setName(roomForm.getName());
    try {
      roomRepository.insert(roomEntity);
    } catch (Exception e) {
      System.out.println("エラー：" + e);
      List<UserEntity> users = userRepository.findAllExcept(currentUser.getId());
      model.addAttribute("users", users);
      model.addAttribute("roomForm", new RoomForm());
      return "chat/rooms/new";
    }

    List<Integer> memberIds = roomForm.getMemberIds();
    for (Integer userId : memberIds) {
      UserEntity userEntity = userRepository.findById(userId);
      RoomUserEntity roomUserEntity = new RoomUserEntity();
      roomUserEntity.setRoom(roomEntity);
      roomUserEntity.setUser(userEntity);
      try {
        roomUserRepository.insert(roomUserEntity);
      } catch (Exception e) {
        System.out.println("エラー：" + e);
        List<UserEntity> users = userRepository.findAllExcept(currentUser.getId());
        model.addAttribute("users", users);
        model.addAttribute("roomForm", new RoomForm());
        return "chat/rooms/new";
      }
    }
        return "redirect:/rooms/" + roomEntity.getId() + "/messages";
  }

  @PostMapping("/rooms/{roomId}/delete")
  public String deleteRoom(@PathVariable Integer roomId) {
    roomRepository.deleteById(roomId);
    return "redirect:/rooms";
  }
}
