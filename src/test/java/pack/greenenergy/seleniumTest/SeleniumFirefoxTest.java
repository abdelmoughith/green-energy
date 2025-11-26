package pack.greenenergy.seleniumTest;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class SeleniumFirefoxTest {

    @Test
    public void openFirefoxPage() {
        // Optional: headless mode
        FirefoxOptions options = new FirefoxOptions();
        // options.addArguments("--headless"); // remove if you want GUI

        WebDriver driver = new FirefoxDriver(options);

        try {
            driver.get("http://localhost:8080/web/login");
            System.out.println("Title: " + driver.getTitle());
        } finally {
            driver.quit();
        }
    }
    @Test
    public void openFirefoxGui() throws InterruptedException {
        // FirefoxDriver uses geckodriver in PATH
        WebDriver driver = new FirefoxDriver();

        // Open your Spring Boot web page
        driver.get("http://localhost:8080/web/login");

        System.out.println("Title: " + driver.getTitle());

        // Keep browser open for manual interaction
        System.out.println("Browser will stay open. Close manually when done.");

        // Wait indefinitely
        Thread.sleep(Long.MAX_VALUE);

        // driver.quit(); // we won’t call quit automatically
    }
}

