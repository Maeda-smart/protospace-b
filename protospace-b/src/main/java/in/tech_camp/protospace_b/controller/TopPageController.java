package in.tech_camp.protospace_b.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import in.tech_camp.protospace_b.custom_user.CustomUserDetail;
import in.tech_camp.protospace_b.entity.PrototypeEntity;
import in.tech_camp.protospace_b.entity.ReadStatusEntity;
import in.tech_camp.protospace_b.entity.UserEntity;
import in.tech_camp.protospace_b.form.PrototypeSearchForm;
import in.tech_camp.protospace_b.repository.PrototypeShowRepository;
import in.tech_camp.protospace_b.repository.UserDetailRepository;
import in.tech_camp.protospace_b.service.ReadStatusService;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class TopPageController {
  private final UserDetailRepository userDetailRepository;
  private final PrototypeShowRepository prototypeShowRepository;
  private final ReadStatusService readStatusService;


  @GetMapping("")
  public String topPage(@AuthenticationPrincipal CustomUserDetail currentUser, Model model) {
    if (currentUser != null) {
      Integer userId = currentUser.getId();
      UserEntity user = userDetailRepository.findById(userId);
      model.addAttribute("user", user);

      List<ReadStatusEntity> readList = readStatusService.findAllByUserId(userId);
      Map<Integer, Boolean> readStatusMap = readList.stream()
        .collect(Collectors.toMap(
          ReadStatusEntity::getPrototypeId,
          r -> true
        ));
      model.addAttribute("readStatusMap", readStatusMap);
    }
    List<PrototypeEntity> prototypes = prototypeShowRepository.showAll();
    PrototypeSearchForm prototypeSearchForm = new PrototypeSearchForm();
    model.addAttribute("prototypes", prototypes);
    model.addAttribute("prototypeSearchForm", prototypeSearchForm);

    return "index";
  }

}
