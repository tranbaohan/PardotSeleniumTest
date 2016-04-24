import junit.framework.Assert;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;


public class PardotSeleniumTest{
	private WebDriver driver;
	private WebDriverWait wait;
	
	@Test
	public void pardotSeleniumTest(){
		if (System.getProperty("os.name").toLowerCase().contains("windows"))
			System.setProperty("webdriver.chrome.driver", "chromeDriver/chromedriver.exe");	// windows chromedriver
		else
			System.setProperty("webdriver.chrome.driver", "chromeDriver/chromedriver");		// mac chromedriver
		driver = new ChromeDriver();
		wait = new WebDriverWait(driver, 10);
		driver.get("https://pi.pardot.com/");
		runTest();
	}
	
	private void runTest() {
        login();
        
        // test list creation
        String listName = "list" + System.currentTimeMillis();
        
        goCreateList(listName);	// create 1st list
        goCreateList(listName);	// create 2nd list same name
        
        WebElement msg = driver.findElement(By.xpath("//*/div[@class='alert alert-error']"));
        Assert.assertNotNull("Error message should be present", msg);
        driver.findElement(By.linkText("Cancel")).click();
        
        renameList(listName);	// rename list
        goCreateList(listName);	// create list same name again
        
        Assert.assertTrue("Error message should not be present", driver.findElements(By.xpath("//*/div[@class='alert alert-error']")).isEmpty());
        
        // test prospect creation
        String prospectName = "prospect" + System.currentTimeMillis();
        goCreateProspectAddToList(prospectName, listName);
        waitForPage(prospectName);
        msg = driver.findElement(By.xpath("//*/div[@class='alert alert-info']"));
        Assert.assertNotNull("Success message should be present", msg);
        retrieveList(listName);
        waitForElement(By.linkText(prospectName));
        WebElement prospect = driver.findElement(By.linkText(prospectName));
		Assert.assertNotNull("Prospect is not added to list", prospect);
        
		sendEmail(listName);
		
        logout();
        
        //Close the browser
        driver.quit();
	}
	
	private void sendEmail(String listName) {
		String url = driver.findElement(By.xpath("//*[@id='dropmenu-marketing']/li/a[text()='Emails']")).getAttribute("href");
		driver.navigate().to(url);
		driver.findElement(By.xpath("//*[@class='btn btn-warning']")).click();
		driver.switchTo().activeElement();
		
		// creating email
		driver.findElement(By.id("name")).sendKeys("email" + System.currentTimeMillis());	
		driver.findElements(By.xpath("//*/button[@class='btn choose-asset']")).get(1).click();
		driver.switchTo().activeElement();
		waitForElement(By.id("ember1136"));
		driver.findElement(By.id("ember1136")).click();
		driver.findElement(By.id("select-asset")).click();
		driver.findElement(By.id("email_type_text_only")).click();
		driver.findElement(By.id("from_template")).click();
		driver.findElement(By.id("save_information")).click();	
		
		// wait for the link to get updated
		(new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return !driver.findElement(By.id("flow_sending")).getAttribute("href").equalsIgnoreCase("javascript:void(0)");
            }
        });
		
		// sending email
		String sendLink = driver.findElement(By.id("flow_sending")).getAttribute("href");
		driver.navigate().to(sendLink);			
		waitForPage("Sending");
		driver.findElement(By.xpath("//*/div[@class='chzn-container chzn-container-single']")).click();
		driver.findElement(By.xpath("//*[@class='chzn-results']/li[text()='" + listName + "']")).click();
		Select senderSelect = new Select(driver.findElement(By.xpath("//*/select[@name='a_sender[]']")));
		senderSelect.selectByIndex(3);
		driver.findElement(By.id("subject_a")).sendKeys(listName);
		// email sending is disable, the Send button is unclickable
		driver.findElement(By.linkText("Send Now")).click();
	}
	
	private void logout(){
		String url = driver.findElement(By.xpath("//*/a[contains(.,' Sign Out')]")).getAttribute("href");
		driver.navigate().to(url);
	}
	
	private void retrieveList(String listName){
		String url = driver.findElement(By.xpath("//*[@id='dropmenu-marketing']/li/ul/li/a[text()='Lists']")).getAttribute("href");
		driver.navigate().to(url);
		driver.findElement(By.linkText(listName)).click();
		waitForPage(listName);
	}
	
	private void goCreateProspectAddToList(String prospectName, String listName) {
		String url = driver.findElement(By.xpath("//*[@id='dropmenu-prospects']/li/a[text()='Prospect List']")).getAttribute("href");
		driver.navigate().to(url);
        waitForPage("Prospects");
        
        //creating prospect
        driver.findElement(By.id("pr_link_create")).click();		
        waitForElement(By.id("default_field_3361"));
        driver.findElement(By.id("default_field_3361")).sendKeys(prospectName);
        driver.findElement(By.id("email")).sendKeys(System.currentTimeMillis() + "@user.com");
        Select campaignSelect = new Select(driver.findElement(By.id("campaign_id")));
        campaignSelect.selectByIndex(3);
        Select profileSelect = new Select(driver.findElement(By.id("profile_id")));
        profileSelect.selectByIndex(3);
        driver.findElement(By.id("score")).sendKeys("5");
        driver.findElement(By.xpath("//*/h4[contains(.,'Lists')]")).click();
        driver.findElement(By.linkText("Select a list to add...")).click();
        driver.findElement(By.xpath("//*[@class='chzn-results']/li[text()='" + listName + "']")).click();
        driver.findElement(By.name("commit")).click();
	}
	
	
	private void login() {
		driver.findElement(By.id("email_address")).sendKeys("pardot.applicant@pardot.com");
    	driver.findElement(By.id("password")).sendKeys("Applicant2012");
        driver.findElement(By.name("commit")).click();
        waitForPage("Dashboard");
	}
	
	private void renameList(String oldName){
		 driver.findElement(By.linkText(oldName)).click();
		 waitForElement(By.linkText("Edit"));
		 driver.findElement(By.linkText("Edit")).click();
		 driver.switchTo().activeElement();
	     waitForElement(By.id("name"));
	     driver.findElement(By.id("name")).sendKeys("_renamed");	
	     driver.findElement(By.id("save_information")).click();
	}
	
	private void goCreateList(String name){
		String url = driver.findElement(By.xpath("//*[@id='dropmenu-marketing']/li/ul/li/a[text()='Lists']")).getAttribute("href");
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
