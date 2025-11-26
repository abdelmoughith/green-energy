package pack.greenenergy.seleniumTest;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.List;

public class GoogleSearchTest {

    @Test
    public void searchGoogle() throws InterruptedException {
        // Ouvre Firefox (GUI)
        WebDriver driver = new FirefoxDriver();

        try {
            // 1️⃣ Accéder à Google
            driver.get("https://www.google.com");

            // 2️⃣ Localiser la barre de recherche et saisir le texte
            WebElement searchBox = driver.findElement(By.name("q"));
            searchBox.sendKeys("Selenium WebDriver");

            // 3️⃣ Appuyer sur Entrée (équivalent de cliquer sur "Recherche Google")
            searchBox.sendKeys(Keys.RETURN);

            // Attendre que les résultats apparaissent
            Thread.sleep(3000);

            // 4️⃣ Vérifier que des résultats s'affichent
            List<WebElement> results = driver.findElements(By.cssSelector("h3"));
            if (results.size() > 0) {
                System.out.println("Résultats trouvés : " + results.size());
                results.forEach(r -> System.out.println(r.getText()));
            } else {
                System.out.println("Aucun résultat trouvé !");
            }

            // Laisser le navigateur ouvert pour interaction manuelle
            System.out.println("Le navigateur reste ouvert pour vérification manuelle.");
            Thread.sleep(Long.MAX_VALUE);

        } finally {
            // driver.quit(); // On ne ferme pas automatiquement
        }
    }
}

