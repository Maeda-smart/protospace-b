package in.tech_camp.protospace_b.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import in.tech_camp.protospace_b.repository.PrototypeNewRepository;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class DeleteController {

  private final PrototypeNewRepository prototypeNewRepository;

  @PostMapping("/prototypes/{prototypeId}/delete")
  public String deletePrototype(@PathVariable("prototypeId") Integer prototypeId) {

    try {
      prototypeNewRepository.deleteById(prototypeId);
    } catch (Exception e) {
      System.out.println("エラー：" + e);
      return "redirect:/";
    }
    return "redirect:/";
  }
}
