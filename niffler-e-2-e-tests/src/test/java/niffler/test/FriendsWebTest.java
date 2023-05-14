package niffler.test;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static niffler.jupiter.annotation.User.UserType.*;

import com.codeborne.selenide.Condition;
import io.qameta.allure.Allure;
import io.qameta.allure.AllureId;
import niffler.jupiter.annotation.User;
import niffler.jupiter.extension.UsersQueueExtension;
import niffler.model.UserJson;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;

@ExtendWith(UsersQueueExtension.class)
public class FriendsWebTest extends BaseWebTest {

  @AllureId("102")
  @Test
  void friendsShouldBeVisible0(@User(userType = WITH_FRIENDS) UserJson user) {
    Allure.step("UserLogin", () -> loginInNiffler(user));

    $("a[href*='friends']").click();
    $$(".table tbody tr").shouldHave(sizeGreaterThan(0));
  }

  @AllureId("103")
  @Test
  void friendsShouldBeVisible1(@User(userType = INVITATION_SENT) UserJson user) {
    Allure.step("UserLogin", () -> loginInNiffler(user));

    $("a[href*='people']").click();
    $$(".table tbody tr").find(Condition.text("Pending invitation"))
        .should(Condition.visible);
  }

  @AllureId("502")
  @Test
  void myTest(@User(userType = WITH_FRIENDS) UserJson user1,
              @User(userType = INVITATION_SENT) UserJson user2,
              @User(userType = INVITATION_RECEIVED) UserJson user3) {

    Allure.step(("UserLogin"), (s) -> {
      s.parameter("Login by: ", user1.getUsername());
      loginInNiffler(user1);
    });
    Allure.step("Open user profile", () -> goToUserProfile());
    Allure.step("Check username", () -> {
      $(By.xpath("//figcaption")).shouldHave(text(user1.getUsername()));
    });
    loguotFromNiffler();

    Allure.step(("UserLogin"), (s) -> {
      s.parameter("Login by: ", user2.getUsername());
      loginInNiffler(user2);
    });
    Allure.step("Open user profile", () -> goToUserProfile());
    Allure.step("Check username", () -> {
      $(By.xpath("//figcaption")).shouldHave(text(user2.getUsername()));
    });
    loguotFromNiffler();

    Allure.step(("UserLogin"), (s) -> {
      s.parameter("Login by: ", user3.getUsername());
      loginInNiffler(user3);
    });
    Allure.step("Open user profile", () -> goToUserProfile());
    Allure.step("Check username", () -> {
      $(By.xpath("//figcaption")).shouldHave(text(user3.getUsername()));
    });
    loguotFromNiffler();
  }

  static private void loginInNiffler(UserJson user) {
    open("http://127.0.0.1:3000/main");
    $("a[href*='redirect']").click();
    $("input[name='username']").setValue(user.getUsername());
    $("input[name='password']").setValue(user.getPassword());
    $("button[type='submit']").click();
  }

  static private void goToUserProfile() {
    open("http://127.0.0.1:3000/profile");
  }

  static private void loguotFromNiffler() {
    Allure.step("Logout from niffler", () -> $(".button-icon_type_logout").click());
  }
}
