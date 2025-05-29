package module;

import com.example.demo.EJBbeans.UserEJB;
import com.example.demo.beans.LoginBean;
import com.example.demo.entities.UserEntity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.ws.rs.core.Response;
import java.util.ResourceBundle;

import static junit.framework.TestCase.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LoginBeanTest {
    @InjectMocks
    LoginBean lb;

    @Mock
    ResourceBundle rb;

    @Mock
    UserEJB userEJB;

    private final String validLogin = "test_user";
    private final String validPassword = "qwerty";
    private final String wrongPassword = "wrong_password";
    private final String alreadyPresentUser = "non_unique_name";
    private UserEntity validUser;

    @Before
    public void setUp() {
        validUser = new UserEntity();
        validUser.setLogin(validLogin);
        validUser.setPassword(validPassword);

        when(userEJB.checkUserInDB(validLogin, validPassword)).thenReturn(true);
        when(userEJB.checkUserInDB(validLogin, wrongPassword)).thenReturn(false);
        when(userEJB.addUserToDB(validLogin, validPassword)).thenReturn(true);
        when(userEJB.addUserToDB(alreadyPresentUser, validPassword)).thenReturn(false);
    }

    @Test
    public void testLoginSuccess() {
        Response response = lb.login(validUser);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertNotNull(response.getCookies());
        assertFalse(response.getCookies().isEmpty());
    }

    @Test
    public void testLoginIncorrectPassword() {
        UserEntity userForgotPassword = validUser;
        userForgotPassword.setPassword(wrongPassword);

        Response response = lb.login(userForgotPassword);
        assertEquals(Response.Status.ACCEPTED.getStatusCode(), response.getStatus());
        assertEquals("Неверный логин или пароль", response.getEntity());
    }

    @Test
    public void testSignInSuccess() {
        Response response = lb.signIn(validUser);
        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        assertNotNull(response.getCookies());
        assertFalse(response.getCookies().isEmpty());
    }

    @Test
    public void testSignInNameAlreadyTaken() {
        UserEntity nonUniqueUser = validUser;
        nonUniqueUser.setLogin(alreadyPresentUser);

        Response response = lb.signIn(nonUniqueUser);
        assertEquals(Response.Status.ACCEPTED.getStatusCode(), response.getStatus());
        assertEquals("Пользователь с таким логином уже существует", response.getEntity());
    }
}
