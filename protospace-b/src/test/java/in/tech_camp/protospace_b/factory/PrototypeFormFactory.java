package in.tech_camp.protospace_b.factory;

import org.springframework.mock.web.MockMultipartFile;

import com.github.javafaker.Faker;

import static in.tech_camp.protospace_b.factory.RandomText.randomTextInRange;
import in.tech_camp.protospace_b.form.PrototypeForm;

public class PrototypeFormFactory {
  private static final Faker faker = new Faker();

  public static PrototypeForm createPrototype() {
    PrototypeForm prototypeForm = new PrototypeForm();
    prototypeForm.setPrototypeName(randomTextInRange(1, 128));
    prototypeForm.setCatchCopy(randomTextInRange(1, 128));
    prototypeForm.setConcept(randomTextInRange(1, 128));
    prototypeForm
        .setImgFile(new MockMultipartFile("image", "image.jpg", "image/jpg", faker.avatar().image().getBytes()));
    return prototypeForm;
  }
}
