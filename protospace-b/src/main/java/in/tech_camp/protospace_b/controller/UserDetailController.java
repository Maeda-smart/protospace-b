package in.tech_camp.protospace_b.controller;

import java.util.ArrayList;
import java.util.HashMap;
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
import in.tech_camp.protospace_b.repository.NiceRepository;
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
  private final NiceRepository niceRepository;
  private final PinRepository pinRepository;


  // ユーザー詳細ページ遷移
  @GetMapping("/users/{userId}")
  public String showMyPage(@PathVariable("userId") Integer userId,
      @AuthenticationPrincipal CustomUserDetail currentUser, Model model) {

    UserEntity users = userDetailRepository.findById(userId);
    model.addAttribute("user", users);

    // ユーザーの投稿取得
    List<PrototypeEntity> prototypes = prototypeShowRepository.showByUserId(userId);
    model.addAttribute("prototypes", prototypes);

    // ユーザーのいいねした投稿取得
    List<PrototypeEntity> bookmarkPrototypes = bookmarkRepository.findBookmarkByUserId(userId);
    model.addAttribute("bookmark", bookmarkPrototypes);

    // ユーザーのプロトタイプごとのいいね数表示
    Map<Integer, Integer> niceCountMap = new HashMap<>();

    for (PrototypeEntity prototype : prototypes) {
      int count = niceRepository.countNiceByPrototypeId(prototype.getId());
      niceCountMap.put(prototype.getId(), count);
    }
    model.addAttribute("niceCountMap", niceCountMap);

    // ブックマークしたプロトタイプごとのいいね数表示
    Map<Integer, Integer> niceBookmark = new HashMap<>();

    for (PrototypeEntity bookmarkPrototype : bookmarkPrototypes) {
      int bookmarkNiceCount = niceRepository.countNiceByPrototypeId(bookmarkPrototype.getId());
      niceBookmark.put(bookmarkPrototype.getId(), bookmarkNiceCount);
    }
    model.addAttribute("niceBookmark", niceBookmark);

    // ログインユーザーが自分のプロトタイプに対し、いいねしたかを判定
    Map<Integer, Boolean> isNiceMap = new HashMap<>();
    if (currentUser != null) {
      Integer loginUserId = currentUser.getId();

      for (PrototypeEntity prototype : prototypes) {
        boolean isNice = niceRepository.existNice(prototype.getId(), loginUserId);
        isNiceMap.put(prototype.getId(), isNice);
      }
    } else {
      // ログインしていない場合はすべてfalseに設定
      for (PrototypeEntity prototype : prototypes) {
        isNiceMap.put(prototype.getId(), false);
      }
    }
    model.addAttribute("isNiceMap", isNiceMap);


    // ログインユーザーがブックマークしたプロトタイプに対し、いいねしたかを判定
    Map<Integer, Boolean> isNiceBookmarks = new HashMap<>();
    if (currentUser != null) {
      Integer loginUserId = currentUser.getId();

      for (PrototypeEntity bookmarkPrototype : bookmarkPrototypes) {
        boolean isNiceBookmark = niceRepository.existNice(bookmarkPrototype.getId(), loginUserId);
        isNiceBookmarks.put(bookmarkPrototype.getId(), isNiceBookmark);
      }
    } else {
      // ログインしていない場合はすべてfalseに設定
      for (PrototypeEntity bookmarkPrototype : bookmarkPrototypes) {
        isNiceBookmarks.put(bookmarkPrototype.getId(), false);
      }
    }
    model.addAttribute("isNiceBookmarks", isNiceBookmarks);


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
