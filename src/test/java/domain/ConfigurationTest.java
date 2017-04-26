/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Pedro Fernandes
 */
public class ConfigurationTest {
    
    public ConfigurationTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }
    
    @Test(expected = IllegalStateException.class)
    public void testSharedFolderNameMustNotBeNull() {
        Configuration instance = new Configuration(80, 30, 50,null, "downloadTest");
    }
    
    @Test(expected = IllegalStateException.class)
    public void testSharedFolderNameMustNotBeEmpty() {
        Configuration instance = new Configuration(80, 30, 50, "", "downloadTest");
    }
    
    @Test(expected = IllegalStateException.class)
    public void testSharedFolderNameMustNotHaveSpaces() {
        Configuration instance = new Configuration(80, 30, 50, "shared Test", "downloadTest");
    }
    
    @Test(expected = IllegalStateException.class)
    public void testDownloadFolderNameMustNotBeNull() {
        Configuration instance = new Configuration(80, 30, 50, "sharedTest", null);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testDownloadFolderNameMustNotBeEmpty() {
        Configuration instance = new Configuration(80, 30, 50, "sharedTest", "");
    }
    
    @Test(expected = IllegalStateException.class)
    public void testDownloadFolderNameMustNotHaveSpaces() {
        Configuration instance = new Configuration(80, 30, 50, "sharedTest", "download Test");
    }
    
    @Test(expected = IllegalStateException.class)
    public void testUDPPortNumberMustNotBeNull() {
        Configuration instance = new Configuration(null, 30, 50, "sharedTest", "downloadTest");
    }
    
    @Test(expected = IllegalStateException.class)
    public void testUDPPortNumberMustNonNegative() {
        Configuration instance = new Configuration(-1, 30, 50, "sharedTest", "downloadTest");
    }
    
    @Test(expected = IllegalStateException.class)
    public void testUDPTimeAnnouncementMustNotBeNull() {
        Configuration instance = new Configuration(80, null, 50, "sharedTest", "downloadTest");
    }
    
    @Test(expected = IllegalStateException.class)
    public void testUDPTimeAnnouncementMustNonNegative() {
        Configuration instance = new Configuration(80, -1, 50, "sharedTest", "downloadTest");
    }
    
    @Test(expected = IllegalStateException.class)
    public void testRefreshFileTimeMustNotBeNull() {
        Configuration instance = new Configuration(80, 30, null, "sharedTest", "downloadTest");
    }
    
    @Test(expected = IllegalStateException.class)
    public void testRefreshFileTimeMustNonNegative() {
        Configuration instance = new Configuration(80, 30, -1, "sharedTest", "downloadTest");
    }
    
    @Test
    public void testConfigurationOK() {
        Configuration instance = new Configuration(80, 30, 50, "sharedTest", "downloadTest");
    }
    
}
