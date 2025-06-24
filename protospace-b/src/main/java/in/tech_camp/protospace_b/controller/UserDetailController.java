package in.tech_camp.protospace_b.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import in.tech_camp.protospace_b.custom_user.CustomUserDetail;
import in.tech_camp.protospace_b.entity.PrototypeEntity;
import in.tech_camp.protospace_b.entity.UserEntity;
import in.tech_camp.protospace_b.repository.BookmarkRepository;
import in.tech_camp.protospace_b.repository.PrototypeShowRepository;
import in.tech_camp.protospace_b.repository.UserDetailRepository;
import lombok.AllArgsConstructor;


@Controller
@AllArgsConstructor
public class UserDetailController {

  private final UserDetailRepository userDetailRepository;
  private final PrototypeShowRepository prototypeShowRepository;
  private final BookmarkRepository bookmarkRepository;

  // ユーザー詳細ページ遷移
  @GetMapping("/users/{userId}")
  public String showMyPage(@PathVariable("userId") Integer userId, @AuthenticationPrincipal CustomUserDetail currentUser, Model model) {

    UserEntity users = userDetailRepository.findById(userId);
    model.addAttribute("user", users);

    Integer loginUser = currentUser.getId();

    // ログインユーザーの投稿取得
    List<PrototypeEntity> prototypes = prototypeShowRepository.showByUserId(users.getId());
    model.addAttribute("prototypes", prototypes);

    // ログインユーザーのいいねした投稿取得
    List<PrototypeEntity> bookmarkPrototypes = bookmarkRepository.findBookmarkByUserId(loginUser);
    model.addAttribute("bookmark", bookmarkPrototypes);
    return "users/detail";
  }
  
}
