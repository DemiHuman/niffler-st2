package niffler.test;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static io.qameta.allure.Allure.step;

import com.codeborne.selenide.Selenide;
import java.io.IOException;
import java.util.UUID;

import niffler.db.dao.NifflerUsersDAO;
import niffler.db.dao.NifflerUsersDAOJdbc;
import niffler.db.entity.UserEntity;
import niffler.jupiter.extension.GenerateNewUserExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

public class LoginNewUserTest extends BaseWebTest {

  private final NifflerUsersDAO usersDAO = new NifflerUsersDAOJdbc();

  @Test
  @ExtendWith(GenerateNewUserExtension.class)
  void loginTest(UserEntity ue) throws IOException {

    String newName = "TheChangeName";
    String newPassword = "55555";

    step("open page", () -> Selenide.open("http://127.0.0.1:3000/main"));
    $("a[href*='redirect']").click();
    $("input[name='username']").setValue(ue.getUsername());
    $("input[name='password']").setValue(ue.getPassword());
    $("button[type='submit']").click();

    $("a[href*='friends']").click();
    $(".header").should(visible).shouldHave(text("Niffler. The coin keeper."));

    step("Read and output in console the user name before changing", () -> {
      System.out.println(usersDAO.readUser(ue.getUsername()).getUsername());
    });

    step("Change username & password", () -> {
      ue.setUsername(newName);
      ue.setPassword(newPassword);
      usersDAO.updateUserById(ue);
    });

    step("Check changing of the name user", () -> {
      Assertions.assertEquals(newName, usersDAO.readUser(ue.getUsername()).getUsername());
      System.out.println(usersDAO.readUser(ue.getUsername()).getUsername());
    });

    step("Delete user from DB and check", () ->
      Assertions.assertEquals(1, usersDAO.deleteUser(ue)));
  }
}