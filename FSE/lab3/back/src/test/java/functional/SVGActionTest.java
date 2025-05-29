package functional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class SVGActionTest {
    WebDriver wd = new EdgeDriver();
    WebDriverWait wdw = new WebDriverWait(wd, Duration.ofSeconds(10));

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
    public void testClickOnGraphAddsPointToTable() {
        WebElement rButton = wd.findElement(By.id("r3"));
        rButton.click();

        wdw.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.cssSelector("table tr"), 1));

        List<WebElement> rowsBefore = wd.findElements(By.cssSelector("table tr"));
        int rowCountBefore = rowsBefore.size();

        WebElement svgGraph = wd.findElement(By.id("graph"));

        Actions builder = new Actions(wd);
        builder.moveToElement(svgGraph, 100, 100).click().perform();

        wdw.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.cssSelector("table tr"), rowCountBefore));

        int newRowCount = wd.findElements(By.cssSelector("table tr")).size();
        assertEquals(rowCountBefore + 1, newRowCount);
    }

    @Test
    public void testClickOnSVGWithoutR() {
        WebElement svgGraph = wd.findElement(By.id("graph"));

        Actions builder = new Actions(wd);
        builder.moveToElement(svgGraph, 100, 100).click().perform();

        WebElement svgError = wd.findElement(By.id("svgError"));
        wdw.until(ExpectedConditions.domPropertyToBe(svgError, "innerHTML", "Задайте значение R, чтобы отправить точку нажатием на график"));
        assertEquals("Задайте значение R, чтобы отправить точку нажатием на график", svgError.getDomProperty("innerHTML"));
    }

    @Test
    public void testSVGDisappear() {
        WebElement rButton = wd.findElement(By.id("r6"));
        rButton.click();

        wdw.until(ExpectedConditions.invisibilityOfElementLocated(
                By.id("graph")));

        assertTrue(wd.findElements(By.id("graph")).isEmpty());
    }

    @After
    public void close() {
        wd.close();
    }
}
