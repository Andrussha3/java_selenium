
package ru.netology;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ValidationTest {
    private WebDriver driver;

    @BeforeAll
    static void setupAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setup() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(15));
    }

    @AfterEach
    void tearDown() {
        driver.quit();
        driver = null;
    }

    @Test
    void shouldShowErrorWithEmptyName() {
        driver.get("http://localhost:9999");

        // Оставляем имя пустым
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79211234567");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector("button.button")).click();

        String text = driver.findElement(By.cssSelector("[data-test-id=name].input_invalid .input__sub")).getText();
        assertTrue(text.equals("Поле обязательно для заполнения"));
    }

    @Test
    void shouldShowErrorWithEmptyPhone() {
        driver.get("http://localhost:9999");

        // Оставляем телефон пустым
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Иванов Иван");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector("button.button")).click();

        String text = driver.findElement(By.cssSelector("[data-test-id=phone].input_invalid .input__sub")).getText();
        assertTrue(text.equals("Поле обязательно для заполнения"));
    }

    @Test
    void shouldShowErrorWithShortPhone() {
        driver.get("http://localhost:9999");

        // Вводим короткий номер телефона
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Иванов Иван");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+792112345");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.cssSelector("button.button")).click();

        String text = driver.findElement(By.cssSelector("[data-test-id=phone].input_invalid .input__sub")).getText();
        assertTrue(text.equals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }
}