package ObjectRepository;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;


public class AccountLogoutPage {
	// Step 1: Declare WebElements
		@FindBy(xpath = "//h1[text()='Account Logout']")
		private WebElement logout_Header;

		
		// Step 2: Initialize WebElements
		public AccountLogoutPage(WebDriver driver) {
			PageFactory.initElements(driver, this);
		}

		// Step 3: Utilize WebElements

		public WebElement getLogout_Header() {
			return logout_Header;
		}

		// Step 4: Business Logic or Generic Methods
		
		
}
