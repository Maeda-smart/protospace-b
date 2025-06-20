package in.tech_camp.protospace_b.form;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class PrototypeForm {
  private String prototypeName;
  private String catchCopy;
  private String concept;
  private MultipartFile img;
}
