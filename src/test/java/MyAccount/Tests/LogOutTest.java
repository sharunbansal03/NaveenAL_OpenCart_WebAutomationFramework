package MyAccount.Tests;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import ObjectRepository.AccountLogoutPage;
import ObjectRepository.AccountPage;
import ObjectRepository.HomePage;
import genericUtilities.BaseClass;

public class LogOutTest extends BaseClass {

	@Test
	public void logOutTest() {
		HomePage hp = new HomePage(driver);
		hp.getMyAccount_dropDwn().click();

		AccountPage ap = new AccountPage(driver);
		ap.clickButtonInMyAccountDrpdwn(driver, "Logout");

		AccountLogoutPage alp = new AccountLogoutPage(driver);
		Assert.assertTrue(alp.getLogout_Header().isDisplayed(), "[ASSERTION FAILED]: LOGOUT FAILED");
		Reporter.log("[ASSERTION PASSED]: LOGOUT SUCCESSFUL", true);
	}
}
