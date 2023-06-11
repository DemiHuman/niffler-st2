package niffler.db.dao;

import java.util.UUID;
import niffler.db.entity.UserEntity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

public interface NifflerUsersDAO {

  PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

  int createUser(UserEntity user);

  int deleteUser(UserEntity user);

  int updateUserById(UserEntity user);

  UserEntity readUser(String userName);

  String getUserId(String userName);

  int removeUser(UserEntity user);

}
