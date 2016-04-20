import org.openqa.selenium.By;


public class PardotSeleniumTest {

	public static void main(String[] args) {
		// Start Firefox Driver
        WebDriver driver = new FirefoxDriver();

        // Navigate to pardot test page
        driver.get("https://pi.pardot.com/");

        // Login
        driver.findElement(By.id("email_address")).sendKeys("pardot.applicant@pardot.com");
    	driver.findElement(By.id("password")).sendKeys("Applicant2012");
        driver.findElement(By.name("submit")).click();

        // Wait for the page to load, timeout after 10 seconds
        (new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return d.getTitle().toLowerCase().startsWith("cheese!");
            }
        });
        
        //Close the browser
        driver.quit();

	}

}
