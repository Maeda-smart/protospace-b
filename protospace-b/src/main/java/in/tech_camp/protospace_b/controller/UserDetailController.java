package in.tech_camp.protospace_b.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import in.tech_camp.protospace_b.entity.PrototypeEntity;
import in.tech_camp.protospace_b.entity.UserEntity;
import in.tech_camp.protospace_b.repository.BookmarkRepository;
import in.tech_camp.protospace_b.repository.NiceRepository;
import in.tech_camp.protospace_b.repository.PrototypeShowRepository;
import in.tech_camp.protospace_b.repository.UserDetailRepository;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class UserDetailController {

  private final UserDetailRepository userDetailRepository;
  private final PrototypeShowRepository prototypeShowRepository;
  private final BookmarkRepository bookmarkRepository;
  private final NiceRepository niceRepository;

  // ユーザー詳細ページ遷移
  @GetMapping("/users/{userId}")
  public String showMyPage(@PathVariable("userId") Integer userId, Model model) {

    UserEntity users = userDetailRepository.findById(userId);
    model.addAttribute("user", users);

    // ユーザーの投稿取得
    List<PrototypeEntity> prototypes = prototypeShowRepository.showByUserId(userId);
    model.addAttribute("prototypes", prototypes);

    // ユーザーのいいねした投稿取得
    List<PrototypeEntity> bookmarkPrototypes = bookmarkRepository.findBookmarkByUserId(userId);
    model.addAttribute("bookmark", bookmarkPrototypes);

    // プロトタイプごとのいいね数表示
    Map<Integer, Integer> niceCountMap = new HashMap<>();

    for (PrototypeEntity prototype : prototypes) {
      int count = niceRepository.countNiceByPrototypeId(prototype.getId());
      niceCountMap.put(prototype.getId(), count);
    }

    model.addAttribute("niceCountMap", niceCountMap);

    return "users/detail";
  }

}
