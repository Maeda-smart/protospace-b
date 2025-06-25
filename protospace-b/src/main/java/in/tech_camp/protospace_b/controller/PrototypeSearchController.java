package in.tech_camp.protospace_b.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import in.tech_camp.protospace_b.entity.PrototypeEntity;
import in.tech_camp.protospace_b.form.PrototypeSearchForm;
import in.tech_camp.protospace_b.repository.PrototypeSearchRepository;
import lombok.AllArgsConstructor;


@Controller
@AllArgsConstructor
public class PrototypeSearchController {
  private final PrototypeSearchRepository prototypeSearchRepository;

  @GetMapping("/prototypes/search")
  public String searchPrototypes(@ModelAttribute("prototypeSearchForm") PrototypeSearchForm prototypeSearchForm, Model model) {
    List<PrototypeEntity> prototypes = prototypeSearchRepository.findByPrototypeName(prototypeSearchForm.getPrototypeName());
    model.addAttribute("prototypes", prototypes);
    model.addAttribute("prototypeSearchForm", prototypeSearchForm);
    System.out.println(prototypes);
      return "prototype/search";
  }
  
}
