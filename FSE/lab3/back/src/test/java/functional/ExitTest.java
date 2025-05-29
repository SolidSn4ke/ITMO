package functional;

import org.junit.After;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static junit.framework.TestCase.assertEquals;

public class ExitTest {
    WebDriver wd = new EdgeDriver();
    WebDriverWait wdw = new WebDriverWait(wd, Duration.ofSeconds(2));

    @Test
    public void testExitSuccess() {
        wd.get("http://localhost:8080/front/");

        WebElement loginField = wd.findElement(By.id("login"));
        WebElement passwordField = wd.findElement(By.id("password"));
        WebElement submitButton = wd.findElement(By.id("formLogin"));

        loginField.sendKeys("a");
        passwordField.sendKeys("123");
        submitButton.click();

        wdw.until(ExpectedConditions.urlToBe("http://localhost:8080/main"));

        WebElement exitButton = wd.findElement(By.id("navi"));
        exitButton.click();

        wdw.until(ExpectedConditions.urlToBe("http://localhost:8080/front/"));
        assertEquals("http://localhost:8080/front/", wd.getCurrentUrl());
    }

    @After
    public void close() {
        wd.close();
    }
}
