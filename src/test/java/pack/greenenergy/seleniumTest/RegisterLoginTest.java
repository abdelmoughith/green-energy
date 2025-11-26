package pack.greenenergy.seleniumTest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

public class RegisterLoginTest {

    private static final String BASE_URL = "http://localhost:8080/web";
    private WebDriver driver;

    private static final String TEST_EMAIL = "test@gmail.com";
    private static final String TEST_PASSWORD = "abc12345";

    @BeforeEach
    public void setup() {
        driver = new FirefoxDriver();
    }

    @Test
    public void testRegister() throws InterruptedException {
        driver.get(BASE_URL + "/register");

        WebElement emailInput = driver.findElement(By.name("email"));
        WebElement passwordInput = driver.findElement(By.name("password"));

        emailInput.sendKeys(TEST_EMAIL);
        passwordInput.sendKeys(TEST_PASSWORD);
        passwordInput.submit();

        System.out.println("Registration submitted for: " + TEST_EMAIL);

        Thread.sleep(2000); // wait for registration to process

        // Browser stays open for manual inspection
        System.out.println("Browser stays open after registration.");
        Thread.sleep(Long.MAX_VALUE);
    }

    @Test
    public void testLogin() throws InterruptedException {
        driver.get(BASE_URL + "/login");

        WebElement emailInput = driver.findElement(By.name("email"));
        WebElement passwordInput = driver.findElement(By.name("password"));

        emailInput.sendKeys(TEST_EMAIL);
        passwordInput.sendKeys(TEST_PASSWORD);
        passwordInput.submit();

        System.out.println("Login submitted for: " + TEST_EMAIL);

        Thread.sleep(2000); // wait for login response

        // Print JWT token if present
        try {
            WebElement tokenArea = driver.findElement(By.tagName("textarea"));
            System.out.println("JWT Token: " + tokenArea.getText());
        } catch (Exception e) {
            System.out.println("No token found (check login page).");
        }

        // Browser stays open for manual inspection
        System.out.println("Browser stays open after login.");
        Thread.sleep(Long.MAX_VALUE);
    }
}
