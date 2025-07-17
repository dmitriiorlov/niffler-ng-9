package guru.qa.niffler.util;

import com.github.javafaker.Faker;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.UdUserJson;
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

    public UdUserJson generateUdUserJson() {
        String firstname = RandomDataUtils.randomName();
        String lastname = RandomDataUtils.randomSurname();
        return new UdUserJson(
                null,
                RandomDataUtils.randomUsername(),
                firstname,
                lastname,
                String.join(" ", firstname, lastname),
                CurrencyValues.RUB,
                "test",
                "test"
        );
    }
}
