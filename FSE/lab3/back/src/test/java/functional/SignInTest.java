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
import java.util.UUID;

import static junit.framework.TestCase.assertEquals;

public class SignInTest {
    WebDriver wd = new EdgeDriver();
    WebDriverWait wdw = new WebDriverWait(wd, Duration.ofSeconds(5));

    @Test
    public void testSignInSuccess() {
        wd.get("http://localhost:8080/front/");

        WebElement loginField = wd.findElement(By.id("login"));
        WebElement passwordField = wd.findElement(By.id("password"));
        WebElement submitButton = wd.findElement(By.id("formSignIn"));

        loginField.sendKeys(UUID.randomUUID().toString());
        passwordField.sendKeys("qwerty");
        submitButton.click();

        wdw.until(ExpectedConditions.urlToBe("http://localhost:8080/main"));
        assertEquals("http://localhost:8080/main", wd.getCurrentUrl());
    }

    @Test
    public void testSignInUserAlreadyPresent() {
        wd.get("http://localhost:8080/front/");

        WebElement loginField = wd.findElement(By.id("login"));
        WebElement passwordField = wd.findElement(By.id("password"));
        WebElement submitButton = wd.findElement(By.id("formSignIn"));

        loginField.sendKeys("a");
        passwordField.sendKeys("123");
        submitButton.click();

        wdw.until(page -> wd.getPageSource().contains("Пользователь с таким логином уже существует"));
        assertEquals("http://localhost:8080/front/", wd.getCurrentUrl());
    }

    @Test
    public void testSignInUserEmptyFields() {
        wd.get("http://localhost:8080/front/");

        WebElement submitButton = wd.findElement(By.id("formSignIn"));
        submitButton.click();

        wdw.until(page -> wd.getPageSource().contains("Заполните все поля"));
        assertEquals("http://localhost:8080/front/", wd.getCurrentUrl());
    }

    @After
    public void close() {
        wd.close();
    }
}
