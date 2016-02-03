package com.sapient.jenkins.param;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testAdd()
    {
    	App app = new App();
        assertEquals(7, app.add(5, 2));
    }
    
    public void testSubtract()
    {
    	App app = new App();
        assertEquals(3, app.subtract(5, 2));
    }
    
    public void testMultiply()
    {
    	App app = new App();
        assertEquals(10, app.multiply(5, 2));
    }
    
    public void testDivision()
    {
    	App app = new App();
        assertEquals(2, app.division(5, 2));
    }
}
