package in.tech_camp.protospace_b.factory;

import com.github.javafaker.Faker;
import in.tech_camp.protospace_b.form.UserForm;

public class UserFormFactory{
  private static final Faker faker = new Faker();

  public static UserForm createUser(){
    UserForm userForm = new UserForm();

    userForm.setNickname("Nick");
    userForm.setEmail("test@example.com");
    userForm.setPassword("Password");
    userForm.setPasswordConfirmation("Password");
    userForm.setProfile("Profile");
    userForm.setAffiliation("Affiliation");
    userForm.setPosition("Position");

    return userForm;
  }
}