package mwang.parkinglot;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTestSuite extends TestCase {
	/**
	 * Create the test case
	 *
	 * @param testName
	 *            name of the test case
	 */

	public AppTestSuite(String testName) throws Exception {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTest(new TestSuite(AppUnitTest.class));
		suite.addTest(new TestSuite(AppMultithreadingUseCaseTest.class));
		return suite;
	}
}
