package niffler.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;
import javax.sql.DataSource;
import niffler.db.DataSourceProvider;
import niffler.db.ServiceDB;
import niffler.db.entity.Authority;
import niffler.db.entity.AuthorityEntity;
import niffler.db.entity.UserEntity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

public class NifflerUsersDAOJdbc implements NifflerUsersDAO {

  private static final DataSource ds = DataSourceProvider.INSTANCE.getDataSource(ServiceDB.NIFFLER_AUTH);
  private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();


  @Override
  public int createUser(UserEntity user) {
    int executeUpdate;

    try (Connection conn = ds.getConnection()) {

      conn.setAutoCommit(false);

      try (PreparedStatement insertUserSt = conn.prepareStatement("INSERT INTO users "
              + "(username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired) "
              + " VALUES (?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
           PreparedStatement insertAuthoritySt = conn.prepareStatement(
                   "INSERT INTO authorities (user_id, authority) VALUES (?, ?)")) {
        insertUserSt.setString(1, user.getUsername());
        insertUserSt.setString(2, pe.encode(user.getPassword()));
        insertUserSt.setBoolean(3, user.getEnabled());
        insertUserSt.setBoolean(4, user.getAccountNonExpired());
        insertUserSt.setBoolean(5, user.getAccountNonLocked());
        insertUserSt.setBoolean(6, user.getCredentialsNonExpired());
        executeUpdate = insertUserSt.executeUpdate();

        final UUID finalUserId;

        try (ResultSet generatedKeys = insertUserSt.getGeneratedKeys()) {
          if (generatedKeys.next()) {
            finalUserId = UUID.fromString(generatedKeys.getString(1));
            user.setId(finalUserId);
          } else {
            throw new SQLException("Creating user failed, no ID present");
          }
        }

        for (AuthorityEntity authority : user.getAuthorities()) {
          insertAuthoritySt.setObject(1, finalUserId);
          insertAuthoritySt.setString(2, authority.getAuthority().name());
          insertAuthoritySt.addBatch();
          insertAuthoritySt.clearParameters();
        }
        insertAuthoritySt.executeBatch();
      } catch (SQLException e) {
        conn.rollback();
        conn.setAutoCommit(true);
        throw new RuntimeException(e);
      }

      conn.commit();
      conn.setAutoCommit(true);

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    return executeUpdate;
  }

  @Override
  public int deleteUser(UserEntity user) {
    int executeDelete;

    final String userID = getUserId(user.getUsername());

    try (Connection conn = ds.getConnection();
         PreparedStatement authoritiesPrStatement = conn.prepareStatement(
                 "DELETE FROM authorities WHERE user_id = '" + userID + "'")) {
      authoritiesPrStatement.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    try (Connection conn = ds.getConnection();
         PreparedStatement usersPrStatement = conn.prepareStatement(
                 "DELETE FROM users WHERE id = '" + userID + "'")) {
      executeDelete = usersPrStatement.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    return executeDelete;
  }

  @Override
  public int updateUserById(UserEntity user) {
    int executeUpdate;

    try (Connection conn = ds.getConnection();
         PreparedStatement st = conn.prepareStatement("UPDATE users "
                 + "SET username = ?, password = ?, enabled = ?, account_non_expired = ?, account_non_locked = ?, credentials_non_expired = ? "
                 + "WHERE id = '" + user.getId() + "'")) {
      st.setString(1, user.getUsername());
      st.setString(2, pe.encode(user.getPassword()));
      st.setBoolean(3, user.getEnabled());
      st.setBoolean(4, user.getAccountNonExpired());
      st.setBoolean(5, user.getAccountNonLocked());
      st.setBoolean(6, user.getCredentialsNonExpired());

      executeUpdate = st.executeUpdate();
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    return executeUpdate;
  }

  @Override
  public UserEntity readUser(String userName) {
    UserEntity user = new UserEntity();
    final String userSql = "SELECT * FROM users WHERE username  = '" + userName + "'";

    try (Connection conn = ds.getConnection();
        Statement st = conn.createStatement();
        ResultSet rs = st.executeQuery(userSql)
    ) {
      while (rs.next()) {
        user.setId(UUID.fromString(rs.getString("id")));
        user.setUsername(rs.getString("username"));
        user.setEnabled(Boolean.valueOf(rs.getString("enabled")));
        user.setAccountNonExpired(Boolean.valueOf(rs.getString("account_non_expired")));
        user.setAccountNonLocked(Boolean.valueOf(rs.getString("account_non_locked")));
        user.setCredentialsNonExpired(Boolean.valueOf(rs.getString("credentials_non_expired")));
      }

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    AuthorityEntity userAuthorities = new AuthorityEntity();
    final String userAuthoritiesSql = "SELECT * FROM authorities WHERE user_id  = '" + user.getId() + "'";

    try (Connection conn = ds.getConnection();
         Statement st = conn.createStatement();
         ResultSet rs = st.executeQuery(userAuthoritiesSql)
    ) {
      while (rs.next()) {
        userAuthorities.setId(UUID.fromString(rs.getString("id")));
        userAuthorities.setAuthority(Authority.valueOf(rs.getString("authority")));
        user.getAuthorities().add(userAuthorities);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }

    return user;
  }

  @Override
  public String getUserId(String userName) {
    try (Connection conn = ds.getConnection();
        PreparedStatement st = conn.prepareStatement("SELECT * FROM users WHERE username = ?")) {
      st.setString(1, userName);
      ResultSet resultSet = st.executeQuery();
      if (resultSet.next()) {
        return resultSet.getString(1);
      } else {
        throw new IllegalArgumentException("Can`t find user by given username: " + userName);
      }
    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public int removeUser(UserEntity user) {
    int executeUpdate;

    try (Connection conn = ds.getConnection()) {

      conn.setAutoCommit(false);

      try (PreparedStatement deleteUserSt = conn.prepareStatement("DELETE FROM users WHERE id = ?");
          PreparedStatement deleteAuthoritySt = conn.prepareStatement(
              "DELETE FROM authorities WHERE user_id = ?")) {
        deleteUserSt.setObject(1, user.getId());
        deleteAuthoritySt.setObject(1, user.getId());

        deleteAuthoritySt.executeUpdate();
        executeUpdate = deleteUserSt.executeUpdate();

      } catch (SQLException e) {
        conn.rollback();
        conn.setAutoCommit(true);
        throw new RuntimeException(e);
      }

      conn.commit();
      conn.setAutoCommit(true);

    } catch (SQLException e) {
      throw new RuntimeException(e);
    }
    return executeUpdate;
  }
}
