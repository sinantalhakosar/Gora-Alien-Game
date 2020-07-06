import com.aliengame.socketserver.Server;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ServerSocketTest {
    @Mock
    Server classToBeTestedSpy;

    @Test
    public void objectCreationTest() {
        Assert.assertTrue(classToBeTestedSpy instanceof Server);
    }
}
