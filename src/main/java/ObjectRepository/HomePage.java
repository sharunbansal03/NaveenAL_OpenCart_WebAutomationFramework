package ObjectRepository;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class HomePage {

	//Step 1: Declare WebElements
	@FindBy(xpath = "//a[@title='My Account']")
	private WebElement myAccount_dropDwn;
	
	@FindBy(xpath="//a[text()='Login']")
	private WebElement login_Lnk;
	
	
	
	//Step 2: Initialize WebElements
	public HomePage(WebDriver driver) {
		PageFactory.initElements(driver, this);
	}
	
	
	//Step 3: Utilize WebElements
	public WebElement getMyAccount_dropDwn() {
		return myAccount_dropDwn;
	}


	public WebElement getLogin_Lnk() {
		return login_Lnk;
	}
	
	
	//Step 4: Business Logic or Generic Methods
	
	/**
	 * This method will click on Login Link under My Account dropdown
	 */
	public void clickMyAccountAndLoginLink() {
		myAccount_dropDwn.click();
		login_Lnk.click();
	}
}
