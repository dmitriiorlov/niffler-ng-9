package guru.qa.niffler.config;

public interface Config {

  static Config getInstance() {
    return LocalConfig.INSTANCE;
  }

  String authJdbcUrl();

  String userdataJdbcUrl();

  String spendJdbcUrl();

  String currencyJdbcUrl();

  String ghUrl();

  String frontUrl();

  String spendUrl();

  String authUrl();

  String gatewayUrl();

  String userdataUrl();
}
