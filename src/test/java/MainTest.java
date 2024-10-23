import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import java.net.*;

public class MainTest {
    static String callUrl = "https://thunga-videoconf-2218.app.100ms.live/meeting/mjf-cegk-ude";
    static AppiumDriver driver;

    static String appPackage = "live.hms.app2";
    static String appActivity = "live.hms.app2.ui.home.HomeActivity";

    public static void LaunchApp() throws MalformedURLException {
        DesiredCapabilities dc = new DesiredCapabilities();

        dc.setCapability("deviceName", "Jayanth's 9 pro");
        dc.setCapability("udid", "10BE2M0GFD00056");
        dc.setCapability("platformVersion", "14");
        dc.setCapability("platformName", "Android");
        dc.setCapability("automationName", "uiAutomator2");

        dc.setCapability("appPackage", appPackage);
        dc.setCapability("appActivity", appActivity);

        URL url = new URL("http://127.0.0.1:4723/");
        driver = new AppiumDriver(url, dc);

        System.out.println("Application Launched successfully");
    }

    public static void main(String[] args) throws MalformedURLException {
        LaunchApp();
    }


}
