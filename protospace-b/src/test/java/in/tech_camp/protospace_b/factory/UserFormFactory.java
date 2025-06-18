package in.tech_camp.protospace_b.factory;

import in.tech_camp.protospace_b.form.UserForm;

public class UserFormFactory{
  public static UserForm createUser(){
    UserForm userForm = new UserForm();

    userForm.setNickname("");
    userForm.setEmail("test@example.com");
    userForm.setPassword("Password");
    userForm.setPasswordConfirmation("Password");
    userForm.setProfile("Profile");
    userForm.setAffiliation("Affiliation");
    userForm.setPosition("Position");

    return userForm;
  }
}