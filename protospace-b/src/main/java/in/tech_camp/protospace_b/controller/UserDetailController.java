package in.tech_camp.protospace_b.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import in.tech_camp.protospace_b.custom_user.CustomUserDetail;
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
    public String showMyPage(@PathVariable("userId") Integer userId,
            @AuthenticationPrincipal CustomUserDetail currentUser, Model model) {

        // ユーザー情報取得
        UserEntity users = userDetailRepository.findById(userId);
        model.addAttribute("user", users);

        // ログインユーザーのID取得
        Integer loginUserId = (currentUser != null) ? currentUser.getId() : null;

        // ユーザーの投稿一覧取得
        List<PrototypeEntity> prototypes = prototypeShowRepository.showByUserId(loginUserId, userId);
        Map<Boolean, List<PrototypeEntity>> partitioned = prototypes.stream()
                .collect(Collectors.partitioningBy(prototype -> prototype.isPin()));
        List<PrototypeEntity> pinnedPrototypes = partitioned.get(true);
        List<PrototypeEntity> unpinnedPrototypes = partitioned.get(false);
        model.addAttribute("pinnedPrototypes", pinnedPrototypes);
        model.addAttribute("unpinnedPrototypes", unpinnedPrototypes);

        // ブックマーク取得
        List<PrototypeEntity> bookmarkPrototypes = prototypeShowRepository.findBookmarkByUserId(loginUserId, userId);
        model.addAttribute("bookmarkPrototypes", bookmarkPrototypes);

        return "users/detail";
    }
}
