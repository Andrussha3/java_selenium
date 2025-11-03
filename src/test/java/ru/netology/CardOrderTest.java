package ru.netology;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


class CardOrderTest {
    private WebDriver driver;

    @BeforeAll
    static void setUpAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.get("http://localhost:9999");
    }

    @AfterEach
    void tearDown() {
        driver.quit();
        driver = null;
    }

    @ParameterizedTest
    @CsvFileSource(files = "src/test/resources/shouldTestV2.csv")
    void shouldSendValidForm(String name, String phone) {
        WebElement form = driver.findElement(By.cssSelector("form"));
        form.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys(name);
        form.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys(phone);
        form.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        form.findElement(By.cssSelector("button")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id=order-success]")).getText();
        assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.", text.trim());
    }

    @ParameterizedTest
    @CsvFileSource(files = "src/test/resources/shouldNotValidate.csv")
    void shouldNotValidateName(String name, String phone) {
        WebElement form = driver.findElement(By.cssSelector("form"));
        form.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys(name);
        form.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys(phone);
        form.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        form.findElement(By.cssSelector("button")).click();
        boolean isTrue = form.findElement((By.cssSelector("span[data-test-id=name].input_invalid"))).isDisplayed();
        String text = form.findElement((By.cssSelector("span[data-test-id=name].input_invalid span.input__sub "))).getText();
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.",text.trim());
        assertTrue(isTrue);

    }

    @ParameterizedTest
    @CsvFileSource(files = "src/test/resources/shouldNotValidatePhone.csv")
    void shouldNotValidatePhone(String name, String phone) {
        WebElement form = driver.findElement(By.cssSelector("form"));
        form.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys(name);
        form.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys(phone);
        form.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        form.findElement(By.cssSelector("button")).click();
        boolean isTrue = form.findElement((By.cssSelector("span[data-test-id=phone].input_invalid"))).isDisplayed();
        String text = form.findElement((By.cssSelector("span[data-test-id=phone].input_invalid span.input__sub "))).getText();
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.",text.trim());
        assertTrue(isTrue);

    }

    @Test
    void shouldNotValidateAgreement() {
        WebElement form = driver.findElement(By.cssSelector("form"));
        form.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Василий");
        form.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79270000000");
        form.findElement(By.cssSelector("button")).click();
        boolean isTrue = form.findElement((By.cssSelector("label.checkbox.input_invalid "))).isDisplayed();

        assertTrue(isTrue);
    }
    @Test
    void shouldNotValidateEmptyName() {
        WebElement form = driver.findElement(By.cssSelector("form"));
        form.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys(" ");
        form.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79270000000");
        form.findElement(By.cssSelector("button")).click();
        boolean isTrue = form.findElement((By.cssSelector("span[data-test-id=name].input_invalid"))).isDisplayed();
        String text = form.findElement((By.cssSelector("span[data-test-id=name].input_invalid span.input__sub "))).getText();
        assertEquals("Поле обязательно для заполнения",text.trim());
        assertTrue(isTrue);


        assertTrue(isTrue);
    }
    @Test
    void shouldNotValidateEmptyPhone() {
        WebElement form = driver.findElement(By.cssSelector("form"));
        form.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Василий");
        form.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys(" ");
        form.findElement(By.cssSelector("button")).click();
        boolean isTrue = form.findElement((By.cssSelector("span[data-test-id=phone].input_invalid"))).isDisplayed();
        String text = form.findElement((By.cssSelector("span[data-test-id=phone].input_invalid span.input__sub "))).getText();
        assertEquals("Поле обязательно для заполнения",text.trim());
        assertTrue(isTrue);
    }
}

