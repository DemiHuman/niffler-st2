package niffler.test;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static io.qameta.allure.Allure.step;
import static org.junit.jupiter.api.Assertions.assertEquals;

import io.qameta.allure.AllureId;
import java.io.IOException;

import niffler.api.UserService;
import niffler.jupiter.annotation.ClasspathUser;
import niffler.model.UserJson;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.By;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class LoginTest extends BaseWebTest {

  private static final String USER_DATA_BASE_URL = "http://127.0.0.1:8089";
  private static final String NIFFLER_BASE_URL = "http://127.0.0.1:3000";

  @ValueSource(strings = {
      "testdata/dima.json",
      "testdata/emma.json"
  })
  @AllureId("104")
  @ParameterizedTest
  void loginTest(@ClasspathUser UserJson user) throws IOException {
    step("open page", () -> open("http://127.0.0.1:3000/main"));
    $("a[href*='redirect']").click();
    $("input[name='username']").setValue(user.getUsername());
    $("input[name='password']").setValue(user.getPassword());
    $("button[type='submit']").click();

    $("a[href*='friends']").click();
    $(".header").should(visible).shouldHave(text("Niffler. The coin keeper."));
  }

  @ValueSource ( strings = {
          "testdata/pavel.json",
          "testdata/tor.json"
  })
  @AllureId("501")
  @ParameterizedTest
  public void updateUserProfileTest(@ClasspathUser UserJson user) throws IOException {
    step("Update user profile", () -> {
      UserService userService = retrofitInit(USER_DATA_BASE_URL, UserService.class);
      Response response = userService.updateUserInfo(user).execute().raw();
      assertEquals(200, response.code(), "Update failed");
    });

    step("Login", () -> {
      open(NIFFLER_BASE_URL);
      $("a[href*='redirect']").click();
      $("input[name='username']").setValue(user.getUsername());
      $("input[name='password']").setValue(user.getPassword());
      $("button[type='submit']").click();
    });

    step("Go to user profile", () -> {
      open(NIFFLER_BASE_URL + "/profile");
    });
    step("Checking user data profile", () -> {
      $(By.name("firstname")).shouldHave(value(user.getFirstname().toUpperCase()));
      $(By.name("surname")).shouldHave(value(user.getSurname().toUpperCase()));
      $(".select-wrapper").shouldHave(text(user.getCurrency().toString()));
    });
  }

  private static <T> T retrofitInit(String baseUrl, Class<T> service) {
    OkHttpClient httpClient = new OkHttpClient.Builder().build();

    Retrofit retrofit = new Retrofit.Builder()
            .client(httpClient)
            .baseUrl(baseUrl)
            .addConverterFactory(JacksonConverterFactory.create())
            .build();

    return retrofit.create(service);
  }
}
