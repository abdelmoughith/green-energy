package pack.greenenergy;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GreenEnergyApplication {

    public static void main(String[] args) {
        SpringApplication.run(GreenEnergyApplication.class, args);
/***
        System.setProperty("webdriver.chrome.driver", "C:\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        try {
            driver.get("Link");
        } finally {
            driver.quit();
        }
 **/
    }


}
