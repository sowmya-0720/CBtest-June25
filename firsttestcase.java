package cbtest1;

import java.time.Duration;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.asserts.SoftAssert;


public class Test1 {

	public static void main(String[] args) {
	
		WebDriver driver = new ChromeDriver();
		SoftAssert softAssert = new SoftAssert();
		driver.get("https://www.cloudbees.com/");
		// Maximize the window
        driver.manage().window().maximize();
        try {
			Thread.sleep(3000);
			//close the cookie notification
			driver.findElement(By.xpath("//button[@aria-label='Close']")).click();
			//click products link
			driver.findElement(By.xpath("//button[normalize-space(text())='Products']")).click();
			//click cloudbees cdro under other products
			driver.findElement(By.xpath("//a[@data-test='navbar.menuLink.products.otherProducts.cloudbeesCD']")).click();
			
			//Verify that Cost Savings has a value of $2m
			WebElement costSavingsElement = driver.findElement(By.xpath("//div[@data-test='stat.primary']//span"));
			String costSavingsValue = costSavingsElement.getText();

	        // Print the retrieved value
	        System.out.println("Cost Savings Value: " + costSavingsValue);
	        softAssert.assertEquals(costSavingsValue, "$2m", "Cost Savings value is incorrect!");
	        
	        //Scroll down, click Auditors / Security
	        WebElement element = driver.findElement(By.xpath("//span[@data-test='button-label' and text()='Auditors / Security']"));
	        // Scroll the element into view using JavaScriptExecutor
	        JavascriptExecutor js = (JavascriptExecutor) driver;
	        js.executeScript("arguments[0].scrollIntoView(true);", element);
	        
	        //Verify the text under Release Governance (Generate single-click audit reports) 
	        //But as per the website there is a different text under Release Governance - Maintain a stable and secure platform
	        String relGovernance = driver.findElement(By.xpath("//*[text()='Release Governance']/following-sibling::h4")).getText();
	        System.out.println(relGovernance);
	        
	        
	        //scroll up
            js.executeScript("window.scrollTo(0, 0);");
            
            //Get the main window handle
            String mainWindow = driver.getWindowHandle();
            
            //Click the link Resources on top > Click Documentation
            driver.findElement(By.xpath("//button[text()='Resources']")).click();
            driver.findElement(By.xpath("//a[@data-test='navbar.menuLink.resources.supportDocumentation.documentation']")).click();
            
            
            Set<String> allWindows = driver.getWindowHandles();
            for (String windowHandle : allWindows) {
            	if (!windowHandle.equals(mainWindow)) {
            		driver.switchTo().window(windowHandle);
            		break;
            	}	
            } 
            
            //Verify that it opens a new tab
            String newTabUrl = driver.getCurrentUrl();
            System.out.println("documentation url: " + newTabUrl);
            Thread.sleep(2000);
            
            //Click in the text field Search all CloudBees Resources
            //Search for the word "Installation"
            driver.findElement(By.xpath("//input[@placeholder='Search all CloudBees Resources']")).sendKeys("installation");
            Thread.sleep(5000);
            
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));  // Maximum wait time of 10 seconds
            
            //Verify that we have pagination options at bottom

            // Wait until the div with the specified class is visible
            WebElement divElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("//div[@class='pb-4_5 mb-4_5 border-bottom']")
            ));
            
            
            if (divElement.isDisplayed()) {
            	
            	WebElement pageItem = wait.until(ExpectedConditions.visibilityOfElementLocated(
                        					By.xpath("//li[@class='page-item active']")));
                // Assert that the <li> element is visible
                softAssert.assertTrue(pageItem.isDisplayed(), "Pagination is not visible.");
                WebElement pageLink = wait.until(ExpectedConditions.visibilityOfElementLocated(
                        By.xpath("//span[@class='page-link'][contains(text(), '1')]/span[text()='(current)']")));
                softAssert.assertTrue(pageLink.isDisplayed(), "Current page 1 is displayed");
                
            } else {
            	System.out.println("No search results available");
            }
            
            

            
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        // Close the browser
        driver.quit();
		
	}

}
