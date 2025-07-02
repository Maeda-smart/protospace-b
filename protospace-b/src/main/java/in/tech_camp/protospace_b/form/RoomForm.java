package in.tech_camp.protospace_b.form;

import java.util.List;

import in.tech_camp.protospace_b.validation.ValidationPriority1;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class RoomForm {
    @NotBlank(message = "Room Name can't be blank", groups = ValidationPriority1.class)
    private String name;

    private List<Integer> memberIds;
}
