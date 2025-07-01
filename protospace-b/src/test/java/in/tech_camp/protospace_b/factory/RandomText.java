package in.tech_camp.protospace_b.factory;

import java.util.Random;

import com.github.javafaker.Faker;

public class RandomText {
    private static final Faker faker = new Faker();

    public static String randomTextInRange(int min, int max) {
        Random rand = new Random();
        StringBuilder sb = new StringBuilder();
        while (sb.length() < min) {
            sb.append(faker.lorem().sentence());
            sb.append(" ");
        }
        int len = min + rand.nextInt(max - min + 1);
        if (sb.length() > max) {
            sb.setLength(max);
        } else {
            sb.setLength(len);
        }
        return sb.toString();
    }
}
