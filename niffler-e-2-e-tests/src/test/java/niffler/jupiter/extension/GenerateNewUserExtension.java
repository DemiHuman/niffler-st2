package niffler.jupiter.extension;

import com.github.javafaker.Faker;
import niffler.db.dao.NifflerUsersDAO;
import niffler.db.dao.NifflerUsersDAOJdbc;
import niffler.db.entity.Authority;
import niffler.db.entity.AuthorityEntity;
import niffler.db.entity.UserEntity;
import org.junit.jupiter.api.extension.*;

import java.util.Arrays;
import java.util.Locale;

public class GenerateNewUserExtension implements ParameterResolver, BeforeEachCallback {

    public static ExtensionContext.Namespace USER_NAMESPACE = ExtensionContext.Namespace
            .create(GenerateNewUserExtension.class);

    private String userName = new Faker(new Locale("en-US")).name().firstName();

    private final NifflerUsersDAO usersDAO = new NifflerUsersDAOJdbc();

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
            UserEntity ue = new UserEntity();
            ue.setUsername(userName);
            ue.setPassword("12345");
            ue.setEnabled(true);
            ue.setAccountNonExpired(true);
            ue.setAccountNonLocked(true);
            ue.setCredentialsNonExpired(true);
            ue.setAuthorities(Arrays.stream(Authority.values()).map(
                    a -> {
                        AuthorityEntity ae = new AuthorityEntity();
                        ae.setAuthority(a);
                        return ae;
                    }
            ).toList());
            usersDAO.createUser(ue);

            context.getStore(USER_NAMESPACE).put("user", ue);
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext,
                                     ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(UserEntity.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext,
                                   ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(USER_NAMESPACE).get("user", UserEntity.class);
    }
}
