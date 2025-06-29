package guru.qa.niffler.util;

import com.github.javafaker.Faker;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;

@UtilityClass
public class DataUtil {
    private final Faker faker = new Faker();

    public String generateUniqueName() {
        return faker.name().firstName().concat(String.valueOf(LocalDateTime.now().hashCode()));
    }
}
