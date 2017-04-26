package demo;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

import java.util.concurrent.TimeUnit;

/**
 * Created by vibhu on 4/26/2017.
 */
public class SeleniumCode {

    public static WebDriver driver = null;
    public static String date;
    String chromeDriverFileLocation = System.getProperty("user.dir") + "\\src\\resources\\chromedriver\\chromedriver.exe";

    public void loginVAApp(){

        System.setProperty("webdriver.chrome.driver", chromeDriverFileLocation);
        driver = new ChromeDriver();
        driver.get("http://application-721436538.ap-northeast-2.elb.amazonaws.com/qava/#/home");
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(5000, TimeUnit.MILLISECONDS);
        driver.findElement(By.id("username")).sendKeys("123");
        driver.findElement(By.id("password")).sendKeys("123");
        driver.findElement(By.xpath("//button[@type='submit']")).click();
    }

    public void sleep(int sleepValue){
        try {
            Thread.sleep(sleepValue);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void homeVAApp(){
        sleep(1000);
        driver.findElement(By.xpath("//div[@class='selectProfile container']")).click();
        driver.findElement(By.xpath("//input[@id='chargeCodeInputBox']")).sendKeys("CCC123");
        driver.findElement(By.xpath("//a[@class='modal-action modal-close waves-effect waves-green']")).click();
        sleep(5000);
        WebElement element = driver.findElement(By.xpath("/html/body/section/ui-view/div[1]/section/div[5]/div[1]/select"));

        Select selectObj =new Select(element);
        String toSelectValue = "string:";

        selectObj.selectByValue(toSelectValue.concat("CCC123"));
        sleep(1000);
        driver.findElement(By.xpath("//button[@type='submit']")).click();
    }

    public void newBooking(){
        sleep(1000);
        date = driver.findElement(By.xpath("/html/body/section/ui-view/div[1]/section/form/div/div[2]/div[2]/div/span")).getText();
    }
}

