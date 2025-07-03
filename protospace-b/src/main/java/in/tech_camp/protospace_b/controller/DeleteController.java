package in.tech_camp.protospace_b.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import in.tech_camp.protospace_b.custom_user.CustomUserDetail;
import in.tech_camp.protospace_b.entity.PrototypeEntity;
import in.tech_camp.protospace_b.repository.PrototypeDeleteRepository;
import in.tech_camp.protospace_b.repository.PrototypeShowRepository;
import in.tech_camp.protospace_b.ImageUrl;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class DeleteController {

    private final PrototypeDeleteRepository prototypeDeleteRepository;
    private final PrototypeShowRepository prototypeShowRepository;
    private final ImageUrl imageUrl;

    @PostMapping("/prototypes/{prototypeId}/delete")
    public String deletePrototype(
            @PathVariable("prototypeId") Integer prototypeId,
            @AuthenticationPrincipal CustomUserDetail currentUser) {

        PrototypeEntity prototypeEntity = prototypeShowRepository.findByPrototypeId(currentUser.getId(), prototypeId);

        Integer ownerUserId = prototypeEntity.getUser().getId();

        if (!ownerUserId.equals(currentUser.getId())) {
            return "redirect:/";
        }

        try {
            prototypeDeleteRepository.deleteById(prototypeId);
            
            String imgPath = prototypeEntity.getImgPath();
            if (imgPath != null && !imgPath.isEmpty()) {
                deleteImageFile(imgPath);
            }

        } catch (Exception e) {
            System.out.println("エラー：" + e);
            return "redirect:/";
        }
        return "redirect:/";
    }

    private void deleteImageFile(String imgPath) {
        if (imgPath == null || imgPath.isEmpty()) return;
        // ダミー画像の保護
        if (imgPath.endsWith("noimg.png")) return;
        String uploadDir = imageUrl.getImageUrl();
        String fileName = imgPath.substring(imgPath.lastIndexOf('/') + 1);
        java.nio.file.Path path = java.nio.file.Paths.get(uploadDir, fileName);
        try {
            java.nio.file.Files.deleteIfExists(path);
        } catch (Exception e) {
            System.out.println("画像ファイルの削除に失敗: " + e.getMessage());
        }
    }
}
