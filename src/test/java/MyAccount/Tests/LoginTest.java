package MyAccount.Tests;

import java.io.IOException;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import ObjectRepository.AccountPage;
import genericUtilities.BaseClass;

public class LoginTest extends BaseClass{
	
	@Test
	public void loginTest() throws IOException {
		
		AccountPage ap= new AccountPage(driver);
		Assert.assertTrue(ap.getMyAccount_Header().isDisplayed(),"[ASSERTION FAILED]: LOGIN FAILED");
		Reporter.log("[ASSERTION PASSED]: LOGIN SUCCESSFUL", true);
		
	}

}
