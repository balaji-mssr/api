package com.tfl.api.bootup;

import org.junit.Before;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

public class WebDriverManager {

    private static WebDriver driver = null;


    @Before
    public static WebDriver getWebDriverInstance() {
        if (driver == null) {
            DesiredCapabilities caps = new DesiredCapabilities();
            caps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,"c://balaji//phantomjs-1.9.1-windows//phantomjs.exe");
            driver = new PhantomJSDriver(caps);
        }
        return driver;
    }

}
