package in.tech_camp.protospace_b.factory;

import com.github.javafaker.Faker;

import static in.tech_camp.protospace_b.factory.RandomText.randomTextInRange;
import in.tech_camp.protospace_b.form.UserForm;

public class UserFormFactory {
    private static final Faker faker = new Faker();

    public static UserForm createUser() {
        UserForm userForm = new UserForm();

        userForm.setEmail(faker.internet().emailAddress());
        userForm.setNickname(faker.name().username());
        userForm.setPassword(faker.internet().password(6, 128));
        userForm.setPasswordConfirmation(userForm.getPassword());
        userForm.setProfile(randomTextInRange(1, 128));
        userForm.setAffiliation(randomTextInRange(1, 128));
        userForm.setPosition(randomTextInRange(1, 128));

        return userForm;
    }
}