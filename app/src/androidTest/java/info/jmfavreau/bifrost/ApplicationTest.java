package info.jmfavreau.bifrost;

import android.app.Application;
import android.test.ApplicationTestCase;

import info.jmfavreau.bifrost.color.FuzzyColor;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    private Application myApplicationTest;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        myApplicationTest = getApplication();
    }

    public void testFuzzyColors() {
        FuzzyColor c1 = new FuzzyColor(0., 0., 0.);
    }
}