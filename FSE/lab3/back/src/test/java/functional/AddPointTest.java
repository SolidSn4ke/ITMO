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

import static junit.framework.TestCase.assertEquals;

public class AddPointTest {
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
    public void testAddPointSuccess() {
        wdw.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.cssSelector("table tr"), 1));

        List<WebElement> rowsBefore = wd.findElements(By.cssSelector("table tr"));
        int rowCountBefore = rowsBefore.size();

        WebElement xButton = wd.findElement(By.id("x3"));
        WebElement yField = wd.findElement(By.id("yInput"));
        WebElement rButton = wd.findElement(By.id("r9"));
        WebElement submitButton = wd.findElement(By.id("submitForm"));

        xButton.click();
        yField.sendKeys("2.2");
        rButton.click();
        submitButton.click();

        new WebDriverWait(wd, Duration.ofSeconds(10))
                .until(ExpectedConditions.numberOfElementsToBeMoreThan(
                        By.cssSelector("table tr"), rowCountBefore));

        List<WebElement> rowsAfter = wd.findElements(By.cssSelector("table tr"));
        assertEquals(rowCountBefore + 1, rowsAfter.size());
    }

    @Test
    public void testAddPointRadiusNotSet() {
        WebElement submitButton = wd.findElement(By.id("submitForm"));
        submitButton.click();

        WebElement rError = wd.findElement(By.id("rError"));
        wdw.until(ExpectedConditions.domPropertyToBe(rError, "innerHTML", "Выберите значение для R"));
        assertEquals("Выберите значение для R", rError.getDomProperty("innerHTML"));
    }

    @Test
    public void testAddPointXNotSet() {
        WebElement submitButton = wd.findElement(By.id("submitForm"));
        submitButton.click();

        WebElement xError = wd.findElement(By.id("xError"));
        wdw.until(ExpectedConditions.domPropertyToBe(xError, "innerHTML", "Выберите значение для x"));
        assertEquals("Выберите значение для x", xError.getDomProperty("innerHTML"));
    }

    @Test
    public void testAddPointYInvalid() {
        WebElement yField = wd.findElement(By.id("yInput"));
        WebElement submitButton = wd.findElement(By.id("submitForm"));

        yField.sendKeys("invalid_value");
        submitButton.click();

        WebElement yError = wd.findElement(By.id("yError"));
        wdw.until(ExpectedConditions.domPropertyToBe(yError, "innerHTML", "y - значение должно быть от -5 до 5"));
        assertEquals("y - значение должно быть от -5 до 5", yError.getDomProperty("innerHTML"));
    }

    @After
    public void close() {
        wd.close();
    }
}
