package userManagement;

import org.testng.annotations.Test;
import utility.SoftAssertUtil;

public class AssertionTry {

    private SoftAssertUtil softAssertUtil;

    @Test(description = "Calling soft assert utility")
    public void verifySomething() {

        softAssertUtil = new SoftAssertUtil();

        softAssertUtil.assertTrue(false, "The login page did not display");
        softAssertUtil.assertAll();

        System.out.println("Executed verifySomething");
    }


}
