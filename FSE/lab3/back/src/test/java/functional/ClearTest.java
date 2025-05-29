package functional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static junit.framework.TestCase.assertTrue;

public class ClearTest {
    WebDriver wd = new EdgeDriver();
    WebDriverWait wdw = new WebDriverWait(wd, Duration.ofSeconds(5));

    @Before
    public void login() {
        wd.get("http://localhost:8080/front/");

        WebElement loginField = wd.findElement(By.id("login"));
        WebElement passwordField = wd.findElement(By.id("password"));
        WebElement submitButton = wd.findElement(By.id("formLogin"));

        loginField.sendKeys("a");
        passwordField.sendKeys("123");
        submitButton.click();

        wdw.until(ExpectedConditions.urlToBe("http://localhost:8080/main"));
    }

    @Test
    public void testClearSuccess() {
        wdw.until(ExpectedConditions.numberOfElementsToBeMoreThan(
                By.cssSelector("table tr"), 1));

        List<WebElement> rowsBefore = wd.findElements(By.cssSelector("table tr"));
        int rowCountBefore = rowsBefore.size();

        WebElement clearButton = wd.findElement(By.id("clear"));
        clearButton.click();

        wdw.until(ExpectedConditions.numberOfElementsToBeLessThan(
                By.cssSelector("table tr"), rowCountBefore));

        List<WebElement> rowsAfter = wd.findElements(By.cssSelector("table tr"));
        int rowCountAfter = rowsAfter.size();

        assertTrue(rowCountAfter < rowCountBefore);
    }

    @After
    public void close() {
        wd.close();
    }
}
