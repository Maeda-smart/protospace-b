package in.tech_camp.protospace_b.form;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import in.tech_camp.protospace_b.validation.ValidationPriority1;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PrototypeForm {

  @NotBlank(message="PrototypeName can't be blank", groups=ValidationPriority1.class)
  private String prototypeName;

  @NotBlank(message="CatchCopy can't be blank", groups=ValidationPriority1.class)
  private String catchCopy;

  @NotBlank(message="Concept can't be blank", groups=ValidationPriority1.class)
  private String concept;

  @NotNull(message="Image can't be blank", groups=ValidationPriority1.class)
  private MultipartFile imgFile;
  private String imgPath;

  private List<String> tag_names;
}
