package in.tech_camp.protospace_b;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.NoArgsConstructor;

@Component
@NoArgsConstructor
public class ImageUrl {
    @Value("${upload.dir}")
    private String url;

    public String getImageUrl() {
        return url;
    }
}
