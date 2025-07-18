package guru.qa.niffler.util;

import com.github.javafaker.Faker;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;

@UtilityClass
public class RandomDataUtils {
    private final Faker faker = new Faker();

    public String randomUsername() {
        return faker.name().firstName().concat(String.valueOf(LocalDateTime.now().hashCode()));
    }

    public String randomName() {
        return faker.name().firstName();
    }

    public String randomSurname() {
        return faker.name().lastName();
    }

    public String randomCategoryName() {
        return faker.animal().name();
    }

    public String randomSentence(int wordsCount) {
        return String.join(" ", faker.lorem().words(wordsCount));
    }
}
