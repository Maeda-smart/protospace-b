package in.tech_camp.protospace_b.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.ui.Model;

import lombok.AllArgsConstructor;

import org.springframework.security.core.annotation.AuthenticationPrincipal;

import in.tech_camp.protospace_b.repository.PinRepository;
import in.tech_camp.protospace_b.entity.PinEntity;
import in.tech_camp.protospace_b.custom_user.CustomUserDetail;
import in.tech_camp.protospace_b.entity.PrototypeEntity;

import org.springframework.web.bind.annotation.RequestBody;

import jakarta.websocket.server.PathParam;


@Controller
@AllArgsConstructor
public class PinController {
  
  private final PinRepository pinRepository;

  @GetMapping("prototypes/{userId}/pin")
  public String getMethodName(@PathVariable("userId") Integer userId, Model model) {
    List<PinEntity> pinEntity = pinRepository.findPinByUserId(userId);

    System.out.println(pinEntity);

    // test
    List<PrototypeEntity> prototype = pinRepository.findByUserId(userId); 
    model.addAttribute("prototype", prototype);

    return "redirect:/";
  }

  @PostMapping("prototypes/{prototypeId}/pin/on")
  public String postMethodName(@PathVariable("prototypeId") Integer prototypeId,
                              @AuthenticationPrincipal CustomUserDetail currentUser
  ) {

    Integer ownerUser = currentUser.getId();
    
    PinEntity pin = new PinEntity();
    pin.setUserId(ownerUser);
    pin.setPrototypeId(prototypeId);

    pinRepository.insert(pin);


    return "redirect:/users/" + ownerUser;
  }
  
  @PostMapping("prototypes/{prototypeId}/pin/off")
  public String deleteMethodName(@PathVariable("prototypeId") Integer prototypeId,
                                @AuthenticationPrincipal CustomUserDetail currentUser) {

    Integer ownerUser = currentUser.getId();

    PinEntity pin = new PinEntity();
    pin.setUserId(ownerUser);
    pin.setPrototypeId(prototypeId);

    pinRepository.delete(pin);

    return "redirect:/users/" + ownerUser;
  }
  
  
}
