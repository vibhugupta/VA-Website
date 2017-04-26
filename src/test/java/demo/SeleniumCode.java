package demo;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

/**
 * Created by vibhu on 4/26/2017.
 */
public class SeleniumCode {

    public static WebDriver driver = null;
    String chromeDriverFileLocation = System.getProperty("user.dir") + "\\src\\resources\\chromedriver\\chromedriver.exe";

    public void loginVAApp(){

        System.setProperty("webdriver.chrome.driver", chromeDriverFileLocation);
        driver = new ChromeDriver();
        driver.get("http://application-721436538.ap-northeast-2.elb.amazonaws.com/qava/#/home");
        driver.manage().window().maximize();
        driver.findElement(By.id("username")).sendKeys("123");
        driver.findElement(By.id("password")).sendKeys("123");
        driver.findElement(By.xpath("//button[@type='submit']")).click();
    }
}

