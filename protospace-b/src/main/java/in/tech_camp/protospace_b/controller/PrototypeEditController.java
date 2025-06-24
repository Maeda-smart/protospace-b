package in.tech_camp.protospace_b.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
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
import in.tech_camp.protospace_b.repository.PrototypeDetailRepository;
import in.tech_camp.protospace_b.repository.PrototypeEditRepository;
import in.tech_camp.protospace_b.repository.UserNewRepository;
import in.tech_camp.protospace_b.validation.ValidationOrder;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class PrototypeEditController {
  
    private final PrototypeEditRepository prototypeEditRepository;
    private final UserNewRepository userNewRepository;
    private final PrototypeDetailRepository prototypeDetailRepository;

    private final ImageUrl imageUrl;

    @GetMapping("/prototype/{prototypeId}/edit")
    public String showPrototypeEdit(@PathVariable("prototypeId") Integer prototypeId, Model model,@AuthenticationPrincipal CustomUserDetail currentUser){

      PrototypeEntity prototypeEntity = prototypeDetailRepository.findByPrototypeId(prototypeId);

      Integer ownerUserId = prototypeEntity.getUser().getId();

      if (!ownerUserId.equals(currentUser.getId())) {
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


        return "prototype/prototypeEdit";
    }

    @PostMapping("/prototypes/{prototypeId}/edit/submit")
    public String editPrototype(
        @PathVariable("prototypeId") Integer prototypeId,
        @ModelAttribute("prototypeForm") @Validated(ValidationOrder.class) PrototypeForm prototypeForm,
        BindingResult result,
        @AuthenticationPrincipal CustomUserDetail currentUser,
        Model model) {

    // バリデーション
    if (result.hasErrors()) {
            List<String> errorMessages = result.getAllErrors().stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());
            model.addAttribute("errorMessages", errorMessages);
            model.addAttribute("prototypeForm", prototypeForm);
            System.out.println(errorMessages);
            return "prototype/prototypeEdit";
        }
    


    try {
      MultipartFile imageFile = prototypeForm.getImgFile();
      String newImgPath;

      System.out.println("imageFile: " + imageFile);

      if(imageFile == null || imageFile.getOriginalFilename() == null || imageFile.getOriginalFilename().isEmpty()) {
        newImgPath = prototypeForm.getImgPath();
        
      } else {
        String uploadDir = imageUrl.getImageUrl();
        String fileName;

        if (imageFile != null && imageFile.getOriginalFilename() != null && !imageFile.getOriginalFilename().isEmpty()) {
            fileName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                        + "_" + imageFile.getOriginalFilename();

            //ターミナルライクな操作をするためのオブジェクト
            java.io.File dir = new java.io.File(uploadDir);

            if (!dir.exists()) dir.mkdirs();
            // 相対パスの指定
            java.nio.file.Path imagePath = java.nio.file.Paths.get(uploadDir, fileName);
            // 保存
            Files.copy(imageFile.getInputStream(), imagePath);

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
      System.out.println(userEntity);

      PrototypeEntity prototype = new PrototypeEntity();
      prototype.setId(prototypeId);
      prototype.setPrototypeName(prototypeForm.getPrototypeName());
      prototype.setCatchCopy(prototypeForm.getCatchCopy());
      prototype.setConcept(prototypeForm.getConcept());
      prototype.setImgPath(newImgPath);
      prototype.setUser(userEntity);

      // 検索用のIDを渡さないといけない
      prototypeEditRepository.update(prototype);

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
}
