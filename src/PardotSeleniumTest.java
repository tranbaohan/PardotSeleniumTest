import java.util.List;

import junit.framework.Assert;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.thoughtworks.selenium.SeleneseTestCase;


public class PardotSeleniumTest{
	private WebDriver driver = new FirefoxDriver();
	private WebDriverWait wait = new WebDriverWait(driver, 10);

	public static void main(String[] args) {
		new PardotSeleniumTest();
	}
	
	public PardotSeleniumTest() {
		driver.get("https://pi.pardot.com/");
		runTest();
	}
	
	private void runTest(){
        login();
        
        // test list creation
        String listUrl = driver.findElement(By.xpath("//*[@id='dropmenu-marketing']/li/ul/li/a[text()='Lists']")).getAttribute("href");
        String randomName = "list" + System.currentTimeMillis();
        
        goCreateList(listUrl, randomName);	// create 1st list
        goCreateList(listUrl, randomName);	// create 2nd list same name
        
        WebElement errorMsg = driver.findElement(By.xpath("//*/div[@class='alert alert-error']"));
        Assert.assertNotNull("Error message should be present", errorMsg);
        driver.findElement(By.id("cancel_information")).click();
        
        renameFirstList("list" + System.currentTimeMillis());	// rename list
        goCreateList(listUrl, randomName);	// create list same name again
        
        errorMsg = driver.findElement(By.xpath("//*/div[@class='alert alert-error']"));
        Assert.assertNull("Error message should not be present", errorMsg);
        
        // test prospect creation
        
        logout();
        
        //Close the browser
        driver.quit();
	}
	
	private void logout(){
		
	}
	
	private void login() {
		driver.findElement(By.id("email_address")).sendKeys("pardot.applicant@pardot.com");
    	driver.findElement(By.id("password")).sendKeys("Applicant2012");
        driver.findElement(By.name("commit")).click();
        waitForPage("Dashboard");
	}
	
	private void renameFirstList(String name){
		 driver.findElement(By.xpath("//*[@id='listx_row_a0'/td/a")).click();
		 driver.findElement(By.xpath("//*/a[text()='Edit']")).click();
		 driver.switchTo().activeElement();
	     waitForElement(By.id("name"));
	     driver.findElement(By.id("name")).sendKeys(name);	
	     driver.findElement(By.id("save_information")).click();
	}
	
	private void goCreateList(String url, String name){
		driver.navigate().to(url);
        waitForPage("Lists");
        driver.findElement(By.id("listxistx_link_create")).click();		//creating list
        driver.switchTo().activeElement();
        waitForElement(By.id("name"));
        driver.findElement(By.id("name")).sendKeys(name);	
        driver.findElement(By.id("save_information")).click();
	}
	
	private void waitForPage(final String title) {
		wait.until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.getTitle().startsWith(title);
            }
        });
	}
	
	private void waitForElement(By elem){
		wait.until(ExpectedConditions.visibilityOfElementLocated(elem));
	}

}
