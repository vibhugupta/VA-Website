package demo;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by vibhu on 4/26/2017.
 */
public class SeleniumCode {

    public static WebDriver driver = null;
    public static String startingDate;
    public static String[] skillNameOtherThanPPT = new String[21];
    int currentTimeVariable;
    int timeSelectorXpathVariable=0;
    String completeTimeSelectorXpathVariable="";
    String chromeDriverFileLocation = System.getProperty("user.dir") + "\\src\\resources\\chromedriver\\chromedriver.exe";

    public String currentDate() {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date dateobj = new Date();
        // System.out.println(df.format(dateobj));
        return (df.format(dateobj));
    }

    public String currentTimeInHours() {
        // DateFormat df = new SimpleDateFormat("HH:mm:ss");
        DateFormat df = new SimpleDateFormat("HH");
        Date dateobj = new Date();
        //  System.out.println(df.format(dateobj));
        return (df.format(dateobj));
    }

    public void loginVAApp() {

        System.setProperty("webdriver.chrome.driver", chromeDriverFileLocation);
        driver = new ChromeDriver();
        driver.get("http://application-721436538.ap-northeast-2.elb.amazonaws.com/qava/#/home");
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(5000, TimeUnit.MILLISECONDS);
        driver.findElement(By.id("username")).sendKeys("123");
        driver.findElement(By.id("password")).sendKeys("123");
        driver.findElement(By.xpath("//button[@type='submit']")).click();
    }

    public void sleep(int sleepValue) {
        try {
            Thread.sleep(sleepValue);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void homeVAApp() {
        sleep(1000);
        driver.findElement(By.xpath("//div[@class='selectProfile container']")).click();
        sleep(1000);
        driver.findElement(By.xpath("//input[@id='chargeCodeInputBox']")).sendKeys("CCC123");
        driver.findElement(By.xpath("//a[@class='modal-action modal-close waves-effect waves-green']")).click();
        sleep(5000);
        WebElement element = driver.findElement(By.xpath("/html/body/section/ui-view/div[1]/section/div[5]/div[1]/select"));

        Select selectObj = new Select(element);
        String toSelectValue = "string:";

        selectObj.selectByValue(toSelectValue.concat("CCC123"));
        sleep(1000);
        driver.findElement(By.xpath("//button[@type='submit']")).click();
    }

    public void newBooking() {

        sleep(1000);
        driver.findElement(By.xpath("//div[@id='newBooking']")).click();
        sleep(2000);
        startingDate = driver.findElement(By.xpath("//select[@id='selectDate']/option[2]")).getText();
        System.out.println("startingDate :" + startingDate);

        // To Validate Other Picklist values Code Starting

        driver.findElement(By.xpath("//div[@id='enableOthers']")).click();
        sleep(2000);
        for (int i = 1; i < 21; i++) {
            skillNameOtherThanPPT[i] = driver.findElement(By.xpath("//ul[@class='dropdown']/li[" + i + "]")).getText();
            //  System.out.println(skill[i]);
        }

        //To Validate Other Picklist values Code Ending

        //To select High End PPT
        driver.findElement(By.xpath("//div[@class='container-fluid']/div[1]/div[2]/div[1]")).click();

        driver.findElement(By.xpath("//input[@id='startTime']")).click();
        currentTimeVariable = Integer.parseInt(currentTimeInHours());
        // currentTimeInHours();
        System.out.println("currentTimeVariable : " + currentTimeVariable);
        if(currentTimeVariable<24){
            timeSelectorXpathVariable=timeSelectorXpathVariable+1;
        }
        completeTimeSelectorXpathVariable =completeTimeSelectorXpathVariable
                .concat("//div[@class='clockpicker-plate']/div[2]/div[")
                .concat(Integer.toOctalString(timeSelectorXpathVariable)).concat("]");
        driver.findElement(By.xpath(completeTimeSelectorXpathVariable)).click();
        driver.findElement(By.xpath("//button[@type='button']")).click();

    }
}

