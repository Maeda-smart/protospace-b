package in.tech_camp.protospace_b.factory;

import java.util.Random;

import com.github.javafaker.Faker;

import in.tech_camp.protospace_b.form.UserForm;

public class UserFormFactory{
  private static final Faker faker = new Faker();

  public static UserForm createUser(){
    UserForm userForm = new UserForm();

    userForm.setNickname(faker.internet().emailAddress());
    userForm.setEmail(faker.name().username());
    userForm.setPassword(faker.internet().password(6, 128));
    userForm.setPasswordConfirmation(userForm.getPassword());
    userForm.setProfile(randomTextInRange(1, 128));
    userForm.setAffiliation(randomTextInRange(1, 128));
    userForm.setPosition(randomTextInRange(1, 128));

    return userForm;
  }

  private static String randomTextInRange(int min, int max){
    Faker faker = new Faker();
    Random rand = new Random();
    StringBuilder sb = new StringBuilder();
    while (sb.length() < min) {
      sb.append(faker.lorem().sentence());
      sb.append("");
    }
    int len = min + rand.nextInt(max - min + 1);
    if (sb.length() > max){
      sb.setLength(max);
    } else{
      sb.setLength(len);
    }
    return sb.toString();
  }
}