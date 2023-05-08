package niffler.test;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
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
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class LoginTest extends BaseWebTest {

  private static final String USER_DATA_BASE_URL = "http://127.0.0.1:8089";

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
  public void updateUserInfoTest(@ClasspathUser UserJson user) throws IOException {
      UserService userService = retrofitInit(USER_DATA_BASE_URL, UserService.class);
      Response response = userService.updateUserInfo(user).execute().raw();
      assertEquals(200, response.code(), "Update failed");
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
