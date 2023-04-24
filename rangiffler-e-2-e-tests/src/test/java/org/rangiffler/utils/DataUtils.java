package org.rangiffler.utils;

import com.github.javafaker.Faker;
import org.rangiffler.model.Country;

import java.util.concurrent.ThreadLocalRandom;

public class DataUtils {

    private static final Faker faker = new Faker();

    public static String generateRandomUsername() {
        return faker.name().username();
    }

    public static String generateRandomPassword() {
        return faker.bothify("????####");
    }

    public static String generateRandomFirstname() {
        return faker.name().firstName();
    }

    public static String generateRandomLastname() {
        return faker.name().lastName();
    }

    public static String generateRandomDescription() {
        return faker.yoda().quote();
    }

    public static String getRandomPhotoPath() {
        int randomNum = ThreadLocalRandom.current().nextInt(1, 11);
        return "img/photo/friends/LOTR" + randomNum + ".jpeg";
    }

    public static Country getRandomCountry() {
        ThreadLocalRandom RANDOM = ThreadLocalRandom.current();
        Country[] countries = Country.values();
        int randomIndex = RANDOM.nextInt(countries.length);
        return countries[randomIndex];
    }
}
