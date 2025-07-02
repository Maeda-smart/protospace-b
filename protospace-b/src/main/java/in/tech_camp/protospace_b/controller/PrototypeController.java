package in.tech_camp.protospace_b.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import in.tech_camp.protospace_b.ImageUrl;
import in.tech_camp.protospace_b.custom_user.CustomUserDetail;
import in.tech_camp.protospace_b.entity.PrototypeEntity;
import in.tech_camp.protospace_b.entity.UserEntity;
import in.tech_camp.protospace_b.form.PrototypeForm;
import in.tech_camp.protospace_b.repository.PrototypeEditRepository;
import in.tech_camp.protospace_b.repository.PrototypeNewRepository;
import in.tech_camp.protospace_b.repository.PrototypeShowRepository;
import in.tech_camp.protospace_b.repository.UserNewRepository;
import in.tech_camp.protospace_b.service.TagService;
import in.tech_camp.protospace_b.validation.ValidationOrder;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class PrototypeController {

    @Autowired
    private final PrototypeNewRepository prototypeNewRepository;
    @Autowired
    private final PrototypeEditRepository prototypeEditRepository;

    @Autowired
    private final UserNewRepository userNewRepository;

    private final TagService tagService;
    private final PrototypeShowRepository prototypeShowRepository;
    private final ImageUrl imageUrl;

    @GetMapping("/prototype/prototypeNew")
    public String showPrototypeNew(Model model) {
        model.addAttribute("prototypeForm", new PrototypeForm());
        model.addAttribute("tags", new ArrayList<>());
        return "prototype/prototypeNew";
    }

    @GetMapping("/prototype/{prototypeId}/edit")
    public String showPrototypeEdit(@PathVariable("prototypeId") Integer prototypeId, Model model,
            @AuthenticationPrincipal CustomUserDetail currentUser) {

        // 投稿主・モデレーター・管理者以外は拒否
        boolean isModeratorOrAdmin = "ROLE_MODERATOR".equals(currentUser.getUser().getRoleName()) ||
            "ROLE_ADMIN".equals(currentUser.getUser().getRoleName());

        PrototypeEntity prototypeEntity = isModeratorOrAdmin
                ? prototypeShowRepository.findByPrototypeIdWithoutUser(prototypeId)
                : prototypeShowRepository.findByPrototypeId(currentUser.getId(), prototypeId);

        if (prototypeEntity == null) {
            return "redirect:/";
        }

        Integer ownerUserId = prototypeEntity.getUser().getId();
        boolean isOwner = ownerUserId.equals(currentUser.getId());

        if (!isOwner && !isModeratorOrAdmin) {
            return "redirect:/";
        }

        PrototypeForm prototypeForm = new PrototypeForm();
        prototypeForm.setPrototypeName(prototypeEntity.getPrototypeName());
        prototypeForm.setCatchCopy(prototypeEntity.getCatchCopy());
        prototypeForm.setConcept(prototypeEntity.getConcept());
        prototypeForm.setImgPath(prototypeEntity.getImgPath());

        model.addAttribute("prototypeForm", prototypeForm);
        model.addAttribute("prototypeId", prototypeId);
        // formに型を追加して渡してもOK
        model.addAttribute("imgPath", prototypeEntity.getImgPath());
        model.addAttribute("tags", prototypeEntity.getTags());

        return "prototype/prototypeEdit";
    }

    @Transactional
    @PostMapping("/prototypes")
    public String createPrototype(
            @ModelAttribute("prototypeForm") @Validated(ValidationOrder.class) PrototypeForm prototypeForm,
            BindingResult result,
            @AuthenticationPrincipal CustomUserDetail currentUser,
            Model model) {

        if (result.hasErrors()) {
            List<String> errorMessages = result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            model.addAttribute("errorMessages", errorMessages);
            model.addAttribute("userForm", prototypeForm);
            return "prototype/prototypeNew";
        }
        MultipartFile imageFile = prototypeForm.getImgFile();

        if (imageFile != null) {
            System.out.println("imageFileName: " + imageFile.getOriginalFilename());
        }

        try {
            String fileName;
            String newImgPath;

            if (imageFile != null && imageFile.getOriginalFilename() != null
                    && !imageFile.getOriginalFilename().isEmpty()) {
                fileName = saveImage(imageFile);
                newImgPath = "/uploads/" + fileName;
            } else {
                model.addAttribute("errorMessage", "画像ファイルが選択されていません。");
                model.addAttribute("prototypeForm", prototypeForm);
                return "prototype/prototypeNew";
            }

            Integer userId = (currentUser != null) ? currentUser.getId() : null;
            if (userId == null) {
                model.addAttribute("errorMessage", "ログインユーザーが取得できませんでした。");
                return "prototype/prototypeNew";
            }
            UserEntity userEntity = userNewRepository.findById(userId);
            if (userEntity == null) {
                model.addAttribute("errorMessage", "ユーザーがデータベースに存在しません。");
                return "prototype/prototypeNew";
            }

            PrototypeEntity prototype = new PrototypeEntity();
            prototype.setPrototypeName(prototypeForm.getPrototypeName());
            prototype.setCatchCopy(prototypeForm.getCatchCopy());
            prototype.setConcept(prototypeForm.getConcept());
            prototype.setImgPath(newImgPath);
            prototype.setUser(userEntity);

            prototypeNewRepository.insert(prototype);
            List<String> tagNames = prototypeForm.getTags();
            if (tagNames == null)
                tagNames = new ArrayList<>();
            tagService.updatePrototypeTags(prototype, tagNames);

        } catch (IOException e) {
            model.addAttribute("errorMessage", "画像の保存に失敗しました。（" + e.getMessage() + "）");
            model.addAttribute("prototypeForm", prototypeForm);
            return "prototype/prototypeNew";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "登録に失敗しました。（" + e.getMessage() + "）");
            model.addAttribute("prototypeForm", prototypeForm);
            return "prototype/prototypeNew";
        }

        return "redirect:/";
    }

    @Transactional
    @PostMapping("/prototypes/{prototypeId}/edit/submit")
    public String editPrototype(
            @PathVariable("prototypeId") Integer prototypeId,
            @ModelAttribute("prototypeForm") @Validated(ValidationOrder.class) PrototypeForm prototypeForm,
            BindingResult result,
            @AuthenticationPrincipal CustomUserDetail currentUser,
            Model model) {

        if (result.hasErrors()) {
            List<String> errorMessages = result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            model.addAttribute("errorMessages", errorMessages);
            model.addAttribute("prototypeForm", prototypeForm);
            return "prototype/prototypeEdit";
        }

        try {
            MultipartFile imageFile = prototypeForm.getImgFile();
            String newImgPath;

            if (imageFile == null || imageFile.getOriginalFilename() == null
                    || imageFile.getOriginalFilename().isEmpty()) {
                newImgPath = prototypeForm.getImgPath();

            } else {
                String fileName;

                if (imageFile != null && imageFile.getOriginalFilename() != null
                        && !imageFile.getOriginalFilename().isEmpty()) {
                    fileName = saveImage(imageFile);
                    newImgPath = "/uploads/" + fileName;
                } else {
                    model.addAttribute("prototypeForm", prototypeForm);
                    return "prototype/prototypeEdit";
                }
            }

            Integer userId = (currentUser != null) ? currentUser.getId() : null;
            if (userId == null) {
                model.addAttribute("errorMessage", "ログインユーザーが取得できませんでした。");
                return "prototype/prototypeEdit";
            }
            UserEntity userEntity = userNewRepository.findById(userId);
            if (userEntity == null) {
                model.addAttribute("errorMessage", "ユーザーがデータベースに存在しません。");
                return "prototype/prototypeEdit";
            }

            PrototypeEntity prototype = new PrototypeEntity();
            prototype.setId(prototypeId);
            prototype.setPrototypeName(prototypeForm.getPrototypeName());
            prototype.setCatchCopy(prototypeForm.getCatchCopy());
            prototype.setConcept(prototypeForm.getConcept());
            prototype.setImgPath(newImgPath);
            prototype.setUser(userEntity);

            // 検索用のIDを渡さないといけない
            prototypeEditRepository.update(prototype);
            List<String> tagNames = prototypeForm.getTags();
            if (tagNames == null)
                tagNames = new ArrayList<>();
            tagService.updatePrototypeTags(prototype, tagNames);

        } catch (IOException e) {
            model.addAttribute("errorMessage", "画像の保存に失敗しました。（" + e.getMessage() + "）");
            model.addAttribute("prototypeForm", prototypeForm);
            return "prototype/prototypeEdit";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "登録に失敗しました。（" + e.getMessage() + "）");
            model.addAttribute("prototypeForm", prototypeForm);
            return "prototype/prototypeEdit";
        }

        return "redirect:/prototypes/" + prototypeId + "/detail";
    }

    private String saveImage(MultipartFile imageFile) throws IOException {
        String UPLOAD_DIR = imageUrl.getImageUrl();
        String fileName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                + "_" + imageFile.getOriginalFilename();

        // ターミナルライクな操作をするためのオブジェクト
        java.io.File dir = new java.io.File(UPLOAD_DIR);

        if (!dir.exists())
            dir.mkdirs();
        // 相対パスの指定
        java.nio.file.Path imagePath = java.nio.file.Paths.get(UPLOAD_DIR, fileName);
        // 保存
        Files.copy(imageFile.getInputStream(), imagePath);
        return fileName;
    }
}
