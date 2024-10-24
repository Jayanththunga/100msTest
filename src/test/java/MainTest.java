import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.*;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.time.Duration;

public class MainTest {
    static String callUrl = "https://thunga-videoconf-2218.app.100ms.live/meeting/mjf-cegk-ude";
    static AppiumDriver driver;

    static String appPackage = "live.hms.app2";
    static String appActivity = "live.hms.app2.ui.home.HomeActivity";

    static WebDriverWait wait10;

    private static void LaunchApp() throws MalformedURLException {
        DesiredCapabilities dc = new DesiredCapabilities();

        dc.setCapability("deviceName", "Jayanth's 9 pro");
        dc.setCapability("udid", "10BE2M0GFD00056");
        dc.setCapability("platformVersion", "14");
        dc.setCapability("platformName", "Android");
        dc.setCapability("automationName", "uiAutomator2");

        dc.setCapability("appPackage", appPackage);
        dc.setCapability("appActivity", appActivity);

        System.out.println("Launching 100ms App");

        URL url = new URL("http://127.0.0.1:4723/");
        driver = new AppiumDriver(url, dc);
        wait10 = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    private static boolean CheckAppLaunched() {
        System.out.println("Check if 100ms App Launched");
        return driver.findElement(By.xpath("//android.widget.ImageView[@content-desc='100ms Logo']")).isDisplayed();
    }

    private static void EnterLinkAndJoin(){
        WebElement linkTextBox = driver.findElement(By.id("live.hms.app2:id/edt_meeting_url"));
        linkTextBox.sendKeys(callUrl);
        if(linkTextBox.getText().equals(callUrl)) System.out.println("Link Entered Successfully");
        else System.out.println("Link not entered");

        WebElement JoinBtn = driver.findElement(By.id("live.hms.app2:id/btn_join_now"));
        JoinBtn.click();
    }

    private static void handlePermissionsPopup() {
        try {
            String popupXpath = "//android.widget.Button[contains(@text, 'Only this time')]";
            wait10.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(popupXpath)));
            driver.findElement(By.xpath(popupXpath)).click();

            System.out.println("Permission granted");
        } catch (Exception e) {
            System.out.println("No permission popup displayed or failed to grant permission");
        }
    }

    private static boolean isVideoActive() {
        try {
            String videoContainerPath = "live.hms.app2:id/video_container_background";
            wait10.until(ExpectedConditions.visibilityOfElementLocated(By.id(videoContainerPath)));
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    private static void checkVideoActive() throws IOException {
        String img1 = "Images/imageBefore.png";
        String img2 = "Images/imageAfter.png";
        takeScreenshot(img1);

        WebElement videoBtn = driver.findElement(By.id("live.hms.app2:id/button_toggle_video"));
        videoBtn.click();

        takeScreenshot(img2);

        if(compareImages(img1,img2)) System.out.println("images are different - Video is being captured... Successful...");
        else System.out.println("images are similar - Video is not being captured...");
    }

    private static void takeScreenshot(String filePath) throws IOException {
        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        File destination = new File(filePath);
        ImageIO.write(ImageIO.read(screenshot), "png", destination);
    }

    public static boolean compareImages(String path1, String path2) throws IOException {
        BufferedImage img1 = ImageIO.read(new File(path1));
        BufferedImage img2 = ImageIO.read(new File(path2));

        if (img1.getWidth() != img2.getWidth() || img1.getHeight() != img2.getHeight()) {
            throw new IOException("Images dimensions mismatch.");
        }

        long difference = 0;
        for (int y = 0; y < img1.getHeight(); y++) {
            for (int x = 0; x < img1.getWidth(); x++) {
                int rgb1 = img1.getRGB(x, y);
                int rgb2 = img2.getRGB(x, y);

                int red1 = (rgb1 >> 16) & 0xff;
                int green1 = (rgb1 >> 8) & 0xff;
                int blue1 = rgb1 & 0xff;

                int red2 = (rgb2 >> 16) & 0xff;
                int green2 = (rgb2 >> 8) & 0xff;
                int blue2 = rgb2 & 0xff;

                difference += Math.abs(red1 - red2);
                difference += Math.abs(green1 - green2);
                difference += Math.abs(blue1 - blue2);
            }
        }

        double totalPixels = img1.getWidth() * img1.getHeight() * 3;
        double avgDifference = difference / totalPixels;
        return ((avgDifference / 255) * 100) > 1.5;
    }

    public static void main(String[] args) throws IOException {
        LaunchApp();

        if(CheckAppLaunched()) System.out.println("App Launched Successfully");
        else System.out.println("App did not launch");

        EnterLinkAndJoin();

        handlePermissionsPopup();
        handlePermissionsPopup();

        if(isVideoActive()) System.out.println("Video container is active");
        else System.out.println("Video container is not active");

        checkVideoActive();
    }
}
