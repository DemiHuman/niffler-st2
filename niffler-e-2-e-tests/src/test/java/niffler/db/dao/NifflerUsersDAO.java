package niffler.db.dao;

import niffler.db.entity.UserEntity;

public interface NifflerUsersDAO {

  int createUser(UserEntity user);

  int deleteUser(UserEntity user);

  int updateUserById(UserEntity user);

  UserEntity readUser(String userName);

  String getUserId(String userName);

}
