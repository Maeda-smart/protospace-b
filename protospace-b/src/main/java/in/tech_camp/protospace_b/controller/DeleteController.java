package in.tech_camp.protospace_b.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import in.tech_camp.protospace_b.repository.PrototypeDeleteRepository;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class DeleteController {

  private final PrototypeDeleteRepository prototypeDeleteRepository;

  @PostMapping("/prototypes/{prototypeId}/delete")
  public String deletePrototype(@PathVariable("prototypeId") Integer prototypeId) {

    try {
      prototypeDeleteRepository.deleteById(prototypeId);
    } catch (Exception e) {
      System.out.println("エラー：" + e);
      return "redirect:/";
    }
    return "redirect:/";
  }
}
