package in.tech_camp.protospace_b.form;

import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class MessageForm {
    private String content;

    private MultipartFile image;

    public void validateMessage(BindingResult result) {
        if ((content == null || content.isEmpty()) && (image == null || image.isEmpty())) {
            result.rejectValue("Content", "error.Message", "Please enter either content or image");
        }
    }
}
