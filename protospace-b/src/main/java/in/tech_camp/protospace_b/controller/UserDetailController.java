package in.tech_camp.protospace_b.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import in.tech_camp.protospace_b.custom_user.CustomUserDetail;
import in.tech_camp.protospace_b.entity.PinEntity;
import in.tech_camp.protospace_b.entity.PrototypeEntity;
import in.tech_camp.protospace_b.entity.UserEntity;
import in.tech_camp.protospace_b.repository.BookmarkRepository;
import in.tech_camp.protospace_b.repository.PinRepository;
import in.tech_camp.protospace_b.repository.PrototypeShowRepository;
import in.tech_camp.protospace_b.repository.UserDetailRepository;
import in.tech_camp.protospace_b.service.PrototypeStatusService;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class UserDetailController {

  private final UserDetailRepository userDetailRepository;
  private final PrototypeShowRepository prototypeShowRepository;
  private final BookmarkRepository bookmarkRepository;
  private final PinRepository pinRepository;
  private final PrototypeStatusService prototypeStatusService;


  // ユーザー詳細ページ遷移
  @GetMapping("/users/{userId}")
  public String showMyPage(@PathVariable("userId") Integer userId,
      @AuthenticationPrincipal CustomUserDetail currentUser, Model model) {

    // ユーザー情報取得
    UserEntity users = userDetailRepository.findById(userId);
    model.addAttribute("user", users);

    // ログインユーザーのID取得
    Integer loginUserId = (currentUser != null) ? currentUser.getId() : null;

    // ユーザーの投稿一覧取得
    List<PrototypeEntity> prototypes = prototypeShowRepository.showByUserId(userId);
    model.addAttribute("prototypes", prototypes);

    // ブックマーク取得
    List<PrototypeEntity> bookmarkPrototypes = bookmarkRepository.findBookmarkByUserId(userId);
    model.addAttribute("bookmarkPrototypes", bookmarkPrototypes);


    // ユーザー投稿のステータス取得
    Map<String, Map<Integer, ?>> prototypeStatus = prototypeStatusService.generatePrototypeStatus(prototypes, loginUserId);
    model.addAttribute("nicePrototype", prototypeStatus.get("niceCountMap"));
    model.addAttribute("isNicePrototype", prototypeStatus.get("isNiceMap"));
    model.addAttribute("prototypeRead", prototypeStatus.get("readStatusMap"));

    // ブックマークのステータス取得
    Map<String, Map<Integer, ?>> bookmarkStatus = prototypeStatusService.generatePrototypeStatus(bookmarkPrototypes, loginUserId);
    model.addAttribute("niceBookmark", bookmarkStatus.get("niceCountMap"));
    model.addAttribute("isBookmarkMap", bookmarkStatus.get("isNiceMap"));
    model.addAttribute("readStatusMap", bookmarkStatus.get("readStatusMap"));
    


    // ピン止め処理
    List<PrototypeEntity> pinnedPrototypes = new ArrayList<>(); 
    List<PrototypeEntity> unpinnedPrototypes = new ArrayList<>();
    // ユーザーが持つピン済みprotoIdの配列を取得
    List<Integer> pinnedIds = pinRepository.findPinByUserId(users.getId()) 
                              .stream() 
                              .map(PinEntity::getPrototypeId) 
                              .collect(Collectors.toList());
    // ピンの有無でリストに分岐格納
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
