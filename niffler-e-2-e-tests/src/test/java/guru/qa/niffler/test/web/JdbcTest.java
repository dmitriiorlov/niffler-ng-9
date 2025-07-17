package guru.qa.niffler.test.web;

import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UdUserJson;
import guru.qa.niffler.service.SpendDbClient;
import guru.qa.niffler.service.UsersDbClient;
import guru.qa.niffler.util.RandomDataUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Date;
import java.util.stream.Stream;

//@Disabled
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

    @ParameterizedTest
    @MethodSource
    @Execution(ExecutionMode.SAME_THREAD)
    void dbTest(UdUserJson user) {
        System.out.println("Cозданный пользователь: " + user);
    }

    private static Stream<Arguments> dbTest() {
        UsersDbClient authDbClient = new UsersDbClient();
        return Stream.of(
                Arguments.of(authDbClient.createUserJdbcXa(RandomDataUtils.generateUdUserJson())),
                Arguments.of(authDbClient.createUserSpringJdbcXa(RandomDataUtils.generateUdUserJson())),
                Arguments.of(authDbClient.createUserJdbcTx(RandomDataUtils.generateUdUserJson())),
                Arguments.of(authDbClient.createUserSpringJdbcTx(RandomDataUtils.generateUdUserJson())),
                Arguments.of(authDbClient.createUserJdbcChainedTx(RandomDataUtils.generateUdUserJson())),
                Arguments.of(authDbClient.createUserSpringJdbcChainedTx(RandomDataUtils.generateUdUserJson())),
                Arguments.of(authDbClient.createUserJdbc(RandomDataUtils.generateUdUserJson())),
                Arguments.of(authDbClient.createUserSpringJdbc(RandomDataUtils.generateUdUserJson()))
        );
    }
}
