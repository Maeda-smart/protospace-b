package in.tech_camp.protospace_b.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import in.tech_camp.protospace_b.custom_user.CustomUserDetail;
import in.tech_camp.protospace_b.entity.BookmarkEntity;
import in.tech_camp.protospace_b.entity.PrototypeEntity;
import in.tech_camp.protospace_b.entity.UserEntity;
import in.tech_camp.protospace_b.repository.BookmarkRepository;
import in.tech_camp.protospace_b.repository.PrototypeShowRepository;
import in.tech_camp.protospace_b.repository.UserNewRepository;
import lombok.AllArgsConstructor;


@Controller
@AllArgsConstructor
public class BookmarkController {
  
  private final BookmarkRepository bookmarkRepository;

  private final UserNewRepository userNewRepository;

  private final PrototypeShowRepository prototypeShowRepository;
  
  // ブックマーク送信メソッド
  @PostMapping("/prototypes/{prototypeId}/bookmark")
  public String bookmarkPrototype(@PathVariable("prototypeId") Integer prototypeId,
    @AuthenticationPrincipal CustomUserDetail currentUser) {

    Integer userId = currentUser.getId();

    // ブックマーク済みかを判別
    boolean isBookmark = bookmarkRepository.existBookmark(prototypeId, userId);
      
    if(isBookmark) {
      // ブックマーク済みなら削除
      bookmarkRepository.deleteBookmark(prototypeId, userId);
    } else {
    // プロトタイプとログインしているユーザー情報を取得
    PrototypeEntity prototype = prototypeShowRepository.findByPrototypeId(prototypeId);
    UserEntity user = userNewRepository.findById(userId);
    
    BookmarkEntity bookmark = new BookmarkEntity();
    bookmark.setPrototype(prototype);
    bookmark.setUser(user);

    // ブックマークを追加
    bookmarkRepository.insert(bookmark);
    }      
    return "redirect:/prototypes/" + prototypeId + "/detail";
  }
  
}
