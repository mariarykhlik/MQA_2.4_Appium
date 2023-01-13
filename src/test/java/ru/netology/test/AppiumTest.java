package ru.netology.test;

import io.appium.java_client.AppiumDriver;
import org.junit.jupiter.api.*;
import org.openqa.selenium.remote.DesiredCapabilities;
import ru.netology.screens.MainScreen;
import ru.netology.screens.TextScreen;

import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
public class AppiumTest {

    private AppiumDriver driver;
    private MainScreen mainScreen;
    private TextScreen textScreen;

    enum Platform {Android, IOS}

    @BeforeAll
    public void createDriver() throws MalformedURLException {
        Platform platform = Platform.Android;
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        if (platform == Platform.Android) {
            desiredCapabilities.setCapability("platformName", "Android");
            desiredCapabilities.setCapability("appium:deviceName", "Galaxy A13");
            desiredCapabilities.setCapability("appium:appPackage", "ru.netology.testing.uiautomator");
            desiredCapabilities.setCapability("appium:appActivity",
                    "ru.netology.testing.uiautomator.MainActivity");
        } else {
            throw new IllegalArgumentException(String.format("Platform is not supported", platform));
        }

        driver = new AppiumDriver(new URL("http://127.0.0.1:4723/wd/hub"), desiredCapabilities);
        mainScreen = new MainScreen(driver);
        textScreen = new TextScreen(driver);

    }

    @Test
    public void shouldNotChangeTextForEmptyString() {
        String initialText = mainScreen.mainScreenInscription.getText();

        mainScreen.inputField.isDisplayed();
        mainScreen.inputField.sendKeys("     ");
        mainScreen.changeInscriptionButton.isDisplayed();
        mainScreen.changeInscriptionButton.click();
        mainScreen.mainScreenInscription.isDisplayed();

        String expected = initialText;
        String actual = mainScreen.mainScreenInscription.getText();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void shouldShowTextOnAnotherScreen() {
        String text = "test";

        mainScreen.inputField.isDisplayed();
        mainScreen.inputField.sendKeys(text);
        mainScreen.openTextScreenButton.isDisplayed();
        mainScreen.openTextScreenButton.click();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        textScreen.textScreenInscription.isDisplayed();

        String expected = text;
        String actual = textScreen.textScreenInscription.getText();
        Assertions.assertEquals(expected, actual);
    }

    @AfterAll
    public void quitDriver() {
        if (driver != null) {
            driver.quit();
        }
    }
}
