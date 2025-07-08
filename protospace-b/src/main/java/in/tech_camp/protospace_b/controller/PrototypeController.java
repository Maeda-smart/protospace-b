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

import jakarta.validation.Validator;
import jakarta.validation.ConstraintViolation;

import java.util.Set;

import org.springframework.web.bind.annotation.RequestParam;

import in.tech_camp.protospace_b.validation.ValidationPriority1;
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
    @Autowired
    private Validator validator;

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

        PrototypeEntity prototypeEntity = prototypeShowRepository.findByPrototypeId(currentUser.getId(), prototypeId);

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

        boolean published = prototypeEntity.isPublished();

        model.addAttribute("prototypeForm", prototypeForm);
        model.addAttribute("prototypeId", prototypeId);
        model.addAttribute("published", published);
        // formに型を追加して渡してもOK
        model.addAttribute("imgPath", prototypeEntity.getImgPath());
        model.addAttribute("tags", prototypeEntity.getTags());

        return "prototype/prototypeEdit";
    }

    @Transactional
    @PostMapping("/prototypes")
    public String createPrototype(
            @ModelAttribute("prototypeForm")  PrototypeForm prototypeForm, BindingResult result,
            @RequestParam("mode") String mode,
            @AuthenticationPrincipal CustomUserDetail currentUser,
            Model model) {

        // 投稿ボタン押下時のみバリデーション発動
        if ("submit".equals(mode)) {

            Set<ConstraintViolation<PrototypeForm>> violations = validator.validate(prototypeForm,
                    ValidationPriority1.class);
            for (ConstraintViolation<PrototypeForm> violation : violations) {
                result.rejectValue(violation.getPropertyPath().toString(), "", violation.getMessage());
            }

            if (prototypeForm.getImgFile() == null ||
                    prototypeForm.getImgFile().getOriginalFilename() == null ||
                    prototypeForm.getImgFile().getOriginalFilename().isEmpty()) {
                result.rejectValue("imgFile", "", "画像ファイルが選択されていません。");
            }
        }

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
            } else if("submit".equals(mode)){
                model.addAttribute("errorMessage", "画像ファイルが選択されていません。");
                model.addAttribute("prototypeForm", prototypeForm);
                return "prototype/prototypeNew";
            } else {
                // ダミー画像
                newImgPath = "/image/noimg.png";
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

            // 下書き保存のnullの部分を空文字に
            if ("draft".equals(mode)) {
                prototypeForm.setPrototypeName(prototypeForm.getPrototypeName() != null ? prototypeForm.getPrototypeName() : "");
                prototypeForm.setCatchCopy(prototypeForm.getCatchCopy() != null ? prototypeForm.getCatchCopy() : "");
                prototypeForm.setConcept(prototypeForm.getConcept() != null ? prototypeForm.getConcept() : "");
                prototype.setPublished(false);
            } else if("submit".equals(mode)){
                prototype.setPublished(true);
            }

            prototype.setPrototypeName(prototypeForm.getPrototypeName());
            prototype.setCatchCopy(prototypeForm.getCatchCopy());
            prototype.setConcept(prototypeForm.getConcept());
            prototype.setImgPath(newImgPath);
            prototype.setUser(userEntity);

            System.out.println(prototype);



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
            @ModelAttribute("prototypeForm") PrototypeForm prototypeForm, BindingResult result,
            @RequestParam("mode") String mode,
            @AuthenticationPrincipal CustomUserDetail currentUser,
            Model model) {

        // 投稿ボタン押下時のみバリデーション発動
        if ("submit".equals(mode)) {
            Set<ConstraintViolation<PrototypeForm>> violations = validator.validate(prototypeForm,
                    ValidationPriority1.class);
            for (ConstraintViolation<PrototypeForm> violation : violations) {
                result.rejectValue(violation.getPropertyPath().toString(), "", violation.getMessage());
            }
        }

        if ("submit".equals(mode)) {
          if ((prototypeForm.getImgFile() == null
              || prototypeForm.getImgFile().getOriginalFilename() == null
              || prototypeForm.getImgFile().getOriginalFilename().isEmpty())
              && prototypeForm.getImgPath().endsWith("noimg.png")) {
              result.rejectValue("imgFile", "", "ダミー画像では公開できません");
              model.addAttribute("prototypeForm", prototypeForm);
              return "prototype/prototypeEdit";
          }
      }

        if (result.hasErrors()) {
            List<String> errorMessages = result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            model.addAttribute("errorMessages", errorMessages);
            model.addAttribute("prototypeForm", prototypeForm);
            return "prototype/prototypeEdit";
        }


        PrototypeEntity beforeEntity = prototypeShowRepository.findByPrototypeId(currentUser.getId(),prototypeId);
        String beforeImgPath = beforeEntity.getImgPath();

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

            if ("draft".equals(mode)) {
                prototypeForm.setPrototypeName(prototypeForm.getPrototypeName() != null ? prototypeForm.getPrototypeName() : "");
                prototypeForm.setCatchCopy(prototypeForm.getCatchCopy() != null ? prototypeForm.getCatchCopy() : "");
                prototypeForm.setConcept(prototypeForm.getConcept() != null ? prototypeForm.getConcept() : "");
                prototype.setPublished(false);
            } else if("submit".equals(mode)){
                prototype.setPublished(true);
            }

            prototype.setId(prototypeId);
            prototype.setPrototypeName(prototypeForm.getPrototypeName());
            prototype.setCatchCopy(prototypeForm.getCatchCopy());
            prototype.setConcept(prototypeForm.getConcept());
            prototype.setImgPath(newImgPath);
            prototype.setUser(userEntity);

            if (beforeImgPath != null && !beforeImgPath.equals(newImgPath)) {
                deleteImageFile(beforeImgPath);
            }

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

    private void deleteImageFile(String imgPath) {
        if (imgPath == null || imgPath.isEmpty()) return;
        // ダミー画像保護
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
