package module;

import com.example.demo.EJBbeans.ResultEJB;
import com.example.demo.EJBbeans.UserEJB;
import com.example.demo.beans.InputsBean;
import com.example.demo.entities.ResultEntity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import javax.ws.rs.core.Response;
import java.util.ResourceBundle;

import static junit.framework.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class InputsBeanTest {
    @Mock
    private UserEJB userEJB = Mockito.mock(UserEJB.class);

    @Mock
    private ResultEJB resultEJB = Mockito.mock(ResultEJB.class);

    @Mock
    private ResourceBundle rb = Mockito.mock(ResourceBundle.class);

    @InjectMocks
    private InputsBean ib = new InputsBean();

    private final String validToken = "valid_token";
    private final String badToken = "bad_token";
    private final String userLogin = "test_user";
    private ResultEntity validResult;

    @Before
    public void setUp() {
        validResult = new ResultEntity();
        validResult.setX("1,3");
        validResult.setY("2.2");
        validResult.setR("3");

        when(userEJB.getAssociatedLogin(validToken)).thenReturn(userLogin);
        when(userEJB.getAssociatedLogin(badToken)).thenReturn(null);
    }

    @Test
    public void testAddToDBSuccess() {
        Response response = ib.addToDB(validToken, validResult);

        assertEquals(Response.Status.CREATED.getStatusCode(), response.getStatus());
        verify(resultEJB).addToDB(eq("1.3"), eq("2.2"), eq("3"), eq(false), eq(userLogin));
    }

    @Test
    public void testAddToDBNoToken() {
        Response response = ib.addToDB(null, validResult);

        assertEquals(Response.Status.ACCEPTED.getStatusCode(), response.getStatus());
        assertEquals("Вы не авторизованы", response.getEntity());
    }

    @Test
    public void testAddToDBUserNotFound() {
        Response response = ib.addToDB(badToken, validResult);
        assertEquals(Response.Status.ACCEPTED.getStatusCode(), response.getStatus());
        assertEquals("Не удалось определить имя пользователя", response.getEntity());
    }

    @Test
    public void testAddToDBInvalidInput() {
        ResultEntity invalidResult = validResult;
        invalidResult.setY("a");

        Response response = ib.addToDB(validToken, invalidResult);
        assertEquals(Response.Status.ACCEPTED.getStatusCode(), response.getStatus());
        assertEquals("Серверу были переданы неверные данные", response.getEntity());
    }

    @Test
    public void testGetEntitiesSuccess() {
        Response response = ib.getEntities();
        assertEquals(Response.Status.ACCEPTED.getStatusCode(), response.getStatus());
    }

    @Test
    public void testClearSuccess() {
        Response response = ib.clear(validToken);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    public void testClearNoToken() {
        Response response = ib.clear(null);
        assertEquals(Response.Status.ACCEPTED.getStatusCode(), response.getStatus());
        assertEquals("Вы не авторизованы", response.getEntity());
    }

    @Test
    public void testClearEmptyToken() {
        Response response = ib.clear("");
        assertEquals(Response.Status.ACCEPTED.getStatusCode(), response.getStatus());
        assertEquals("Вы не авторизованы", response.getEntity());
    }

    @Test
    public void testClearUserNotFound() {
        Response response = ib.clear(badToken);
        assertEquals(Response.Status.ACCEPTED.getStatusCode(), response.getStatus());
        assertEquals("Не удалось определить имя пользователя", response.getEntity());
    }
}
