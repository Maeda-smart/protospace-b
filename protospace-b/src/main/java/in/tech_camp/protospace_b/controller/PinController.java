package in.tech_camp.protospace_b.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import in.tech_camp.protospace_b.custom_user.CustomUserDetail;
import in.tech_camp.protospace_b.entity.PinEntity;
import in.tech_camp.protospace_b.repository.PinRepository;
import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class PinController {

    private final PinRepository pinRepository;

    @PostMapping("prototypes/{prototypeId}/pin/on")
    public String postMethodName(@PathVariable("prototypeId") Integer prototypeId,
            @AuthenticationPrincipal CustomUserDetail currentUser) {

        Integer ownerUser = currentUser.getId();

        PinEntity pin = new PinEntity();
        pin.setUserId(ownerUser);
        pin.setPrototypeId(prototypeId);

        pinRepository.insert(pin);

        return "redirect:/prototypes/" + prototypeId + "/detail";
    }

    @PostMapping("prototypes/{prototypeId}/pin/off")
    public String deleteMethodName(@PathVariable("prototypeId") Integer prototypeId,
            @AuthenticationPrincipal CustomUserDetail currentUser) {

        Integer ownerUser = currentUser.getId();

        PinEntity pin = new PinEntity();
        pin.setUserId(ownerUser);
        pin.setPrototypeId(prototypeId);

        pinRepository.delete(pin);

        return "redirect:/prototypes/" + prototypeId + "/detail";
    }
}
