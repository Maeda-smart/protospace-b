package in.tech_camp.protospace_b.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import in.tech_camp.protospace_b.entity.PinEntity;
import in.tech_camp.protospace_b.entity.PrototypeEntity;
import in.tech_camp.protospace_b.entity.UserEntity;
import in.tech_camp.protospace_b.repository.BookmarkRepository;
import in.tech_camp.protospace_b.repository.PinRepository;
import in.tech_camp.protospace_b.repository.PrototypeShowRepository;
import in.tech_camp.protospace_b.repository.UserDetailRepository;
import lombok.AllArgsConstructor;


@Controller
@AllArgsConstructor
public class UserDetailController {

  private final UserDetailRepository userDetailRepository;
  private final PrototypeShowRepository prototypeShowRepository;
  private final BookmarkRepository bookmarkRepository;
  private final PinRepository pinRepository;

  // ユーザー詳細ページ遷移
  @GetMapping("/users/{userId}")
  public String showMyPage(@PathVariable("userId") Integer userId,Model model) {

    UserEntity users = userDetailRepository.findById(userId);
    model.addAttribute("user", users);

    // ログインユーザーの投稿取得
    List<PrototypeEntity> prototypes = prototypeShowRepository.showByUserId(userId);
    model.addAttribute("prototypes", prototypes);

    // ログインユーザーのいいねした投稿取得
    List<PrototypeEntity> bookmarkPrototypes = bookmarkRepository.findBookmarkByUserId(userId);
    model.addAttribute("bookmark", bookmarkPrototypes);
    

    // ピン止め処理
    List<PrototypeEntity> pinnedPrototypes = new ArrayList<>(); 
    List<PrototypeEntity> unpinnedPrototypes = new ArrayList<>();

    // ユーザーが持つピン済みprotoIdの配列を取得
    List<Integer> pinnedIds = pinRepository.findPinByUserId(users.getId()) 
                              .stream() 
                              .map(PinEntity::getPrototypeId) 
                              .collect(Collectors.toList());

    for (PrototypeEntity proto : prototypes) { 
      if (pinnedIds.contains(proto.getId())) { 
        pinnedPrototypes.add(proto); 
      } else { 
        unpinnedPrototypes.add(proto); 
      } }

    model.addAttribute("pinnedPrototypes", pinnedPrototypes);
    model.addAttribute("unpinnedPrototypes", unpinnedPrototypes);

    return "users/detail";
  }
}
