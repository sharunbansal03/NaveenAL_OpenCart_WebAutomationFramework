package ObjectRepository;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Reporter;


public class AccountPage {
	// Step 1: Declare WebElements
		@FindBy(xpath = "//h2[text()='My Account']")
		private WebElement myAccount_Header;

		
		// Step 2: Initialize WebElements
		public AccountPage(WebDriver driver) {
			PageFactory.initElements(driver, this);
		}

		// Step 3: Utilize WebElements

		public WebElement getMyAccount_Header() {
			return myAccount_Header;
		}

		// Step 4: Business Logic or Generic Methods
		
		/**
		 * This method clicks on Link from MyAccount dropdown
		 * @param driver
		 * @param linkText
		 */
		public void clickButtonInMyAccountDrpdwn(WebDriver driver, String linkText) {
			driver.findElement(By.xpath("//li[@class='dropdown open']//a[text()='"+linkText+"']")).click();
			Reporter.log("STEP: Clicked on "+linkText+" from My account dropdown",true);
		}

}
