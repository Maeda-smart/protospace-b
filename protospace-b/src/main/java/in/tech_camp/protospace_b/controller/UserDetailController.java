package in.tech_camp.protospace_b.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import in.tech_camp.protospace_b.entity.PrototypeEntity;
import in.tech_camp.protospace_b.entity.UserEntity;
import in.tech_camp.protospace_b.repository.PrototypeShowRepository;
import in.tech_camp.protospace_b.repository.UserDetailRepository;
import lombok.AllArgsConstructor;


@Controller
@AllArgsConstructor
public class UserDetailController {

  private final UserDetailRepository userDetailRepository;
  private final PrototypeShowRepository prototypeShowRepository;

  // ユーザー詳細ページ遷移
  @GetMapping("/users/{userId}")
  public String showMyPage(@PathVariable("userId") Integer userId, Model model) {
    UserEntity users = userDetailRepository.findById(userId);
    
    model.addAttribute("user", users);
    List<PrototypeEntity> prototypes = prototypeShowRepository.showByUserId(users.getId());
    model.addAttribute("prototypes", prototypes);
    return "users/detail";
  }
}
