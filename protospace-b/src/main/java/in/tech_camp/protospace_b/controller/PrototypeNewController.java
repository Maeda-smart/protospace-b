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
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import in.tech_camp.protospace_b.ImageUrl;
import in.tech_camp.protospace_b.custom_user.CustomUserDetail;
import in.tech_camp.protospace_b.entity.PrototypeEntity;
import in.tech_camp.protospace_b.entity.UserEntity;
import in.tech_camp.protospace_b.form.PrototypeForm;
import in.tech_camp.protospace_b.repository.PrototypeNewRepository;
import in.tech_camp.protospace_b.repository.TagRepository;
import in.tech_camp.protospace_b.repository.UserNewRepository;
import in.tech_camp.protospace_b.validation.ValidationOrder;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor

public class PrototypeNewController {
    @Autowired
    private final PrototypeNewRepository prototypeNewRepository;

    @Autowired
    private final UserNewRepository userNewRepository;

    @Autowired
    private final TagRepository tagRepository;

    private final ImageUrl imageUrl;

    @GetMapping("/prototype/prototypeNew")
    public String showPrototypeNew(Model model) {
        model.addAttribute("prototypeForm", new PrototypeForm());
        model.addAttribute("tags", new ArrayList<>());
        return "prototype/prototypeNew";
    }

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
            System.out.println("error");
            errorMessages.forEach(item -> {System.out.println(item);});
            model.addAttribute("errorMessages", errorMessages);
            model.addAttribute("userForm", prototypeForm);
            return "prototype/prototypeNew";
        }
        MultipartFile imageFile = prototypeForm.getImgFile();
        System.out.println("imageFile: " + imageFile);

        if (imageFile != null) {
            System.out.println("imageFileName: " + imageFile.getOriginalFilename());
        }

        try {
            String uploadDir = imageUrl.getImageUrl();
            String fileName;
            if (imageFile != null && imageFile.getOriginalFilename() != null
                    && !imageFile.getOriginalFilename().isEmpty()) {
                fileName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                        + "_" + imageFile.getOriginalFilename();
                java.io.File dir = new java.io.File(uploadDir);

                if (!dir.exists())
                    dir.mkdirs();
                java.nio.file.Path imagePath = java.nio.file.Paths.get(uploadDir, fileName);
                Files.copy(imageFile.getInputStream(), imagePath);
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
            System.out.println("userEntity: " + userEntity);
            if (userEntity == null) {
                model.addAttribute("errorMessage", "ユーザーがデータベースに存在しません。");
                return "prototype/prototypeNew";
            }

            PrototypeEntity prototype = new PrototypeEntity();
            prototype.setPrototypeName(prototypeForm.getPrototypeName());
            prototype.setCatchCopy(prototypeForm.getCatchCopy());
            prototype.setConcept(prototypeForm.getConcept());
            prototype.setImgPath("/uploads/" + fileName);
            prototype.setUser(userEntity);

            prototypeNewRepository.insert(prototype);

        } catch (IOException e) {
            model.addAttribute("errorMessage", "画像の保存に失敗しました。（" + e.getMessage() + "）");
            model.addAttribute("prototypeForm", prototypeForm);
            return "prototype/prototypeNew";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "登録に失敗しました。（" + e.getMessage() + "）");
            model.addAttribute("prototypeForm", prototypeForm);
            return "prototype/prototypeNew";
        }

        try {
            // tagRepository
            prototypeForm.getTag_names().forEach(item -> {
                System.out.println(item);
            });
        } catch (Exception e) {
        }

        return "redirect:/";
    }
}
