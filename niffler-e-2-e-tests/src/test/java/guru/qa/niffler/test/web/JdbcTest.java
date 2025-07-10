package guru.qa.niffler.test.web;

import com.github.javafaker.Faker;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.auth.UserEntity;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.service.AuthDbClient;
import guru.qa.niffler.service.SpendDbClient;
import guru.qa.niffler.util.RandomDataUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

@Disabled
@Slf4j
public class JdbcTest {

  @Test
  void txTest() {
    SpendDbClient spendDbClient = new SpendDbClient();

    SpendJson spend = spendDbClient.createSpend(
        new SpendJson(
            null,
            new Date(),
            new CategoryJson(
                null,
                "cat-name-tx-2",
                "duck",
                false
            ),
            CurrencyValues.RUB,
            1000.0,
            "spend-name-tx",
            null
        )
    );

    System.out.println(spend);
  }

    @Test
    void dbTest() {
        AuthDbClient authDbClient = new AuthDbClient();

        AuthorityEntity aeRead = new AuthorityEntity();
        aeRead.setId(null);
        aeRead.setAuthority(Authority.READ);

        AuthorityEntity aeWrite = new AuthorityEntity();
        aeWrite.setAuthority(Authority.WRITE);
        aeWrite.setId(null);

        UserEntity userEntity = new UserEntity();
        userEntity.setId(null);
        userEntity.setUsername(RandomDataUtils.randomUsername());
        userEntity.setPassword(new Faker().letterify("?????"));
        userEntity.setEnabled(true);
        userEntity.setAccountNonExpired(true);
        userEntity.setAccountNonLocked(true);
        userEntity.setCredentialsNonExpired(true);
        userEntity.setAuthorities(List.of(aeWrite, aeRead));

        UserEntity user = authDbClient.createUser(userEntity);

        log.info("USER:" + user);
    }
}
