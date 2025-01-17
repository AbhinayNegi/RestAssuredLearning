package utility;

import org.testng.asserts.SoftAssert;

public class SoftAssertUtil {

    private final SoftAssert softAssert;

    public SoftAssertUtil() {
        this.softAssert = new SoftAssert();
    }

    public void assertEquals(Object actual, Object expected, String message) {
        softAssert.assertEquals(actual, expected, message);
    }

    public void assertTrue(boolean condition, String message) {
        softAssert.assertTrue(condition, message);
    }

    public void assertAll() {
        softAssert.assertAll();
    }
}
