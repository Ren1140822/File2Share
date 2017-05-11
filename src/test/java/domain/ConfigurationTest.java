/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import org.junit.*;

/**
 * @author Pedro Fernandes
 */
public class ConfigurationTest {
    
    String[] ignoreFiles = {"sys", "etc", "bootmgr.efi"}; 
    String[] ignoreFilesStartingWith = {".", "_", "~"};
    String[] ignoreFilesWithExtension = {"store", "db"};
    String[] empty;

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
        Configuration instance = new Configuration(80, 30, 50, null, "downloadTest", true, true, ignoreFiles, ignoreFilesStartingWith,ignoreFilesWithExtension);
    }

    @Test(expected = IllegalStateException.class)
    public void testSharedFolderNameMustNotBeEmpty() {
        Configuration instance = new Configuration(80, 30, 50, "", "downloadTest", true, true, ignoreFiles, ignoreFilesStartingWith,ignoreFilesWithExtension);
    }

    @Test(expected = IllegalStateException.class)
    public void testSharedFolderNameMustNotHaveSpaces() {
        Configuration instance = new Configuration(80, 30, 50, "shared Test", "downloadTest", true, true, ignoreFiles, ignoreFilesStartingWith,ignoreFilesWithExtension);
    }

    @Test(expected = IllegalStateException.class)
    public void testDownloadFolderNameMustNotBeNull() {
        Configuration instance = new Configuration(80, 30, 50, "sharedTest", null, true, true, ignoreFiles, ignoreFilesStartingWith,ignoreFilesWithExtension);
    }

    @Test(expected = IllegalStateException.class)
    public void testDownloadFolderNameMustNotBeEmpty() {
        Configuration instance = new Configuration(80, 30, 50, "sharedTest", "", true, true, ignoreFiles, ignoreFilesStartingWith,ignoreFilesWithExtension);
    }

    @Test(expected = IllegalStateException.class)
    public void testDownloadFolderNameMustNotHaveSpaces() {
        Configuration instance = new Configuration(80, 30, 50, "sharedTest", "download Test", true, true, ignoreFiles, ignoreFilesStartingWith,ignoreFilesWithExtension);
    }

    @Test(expected = IllegalStateException.class)
    public void testUDPPortNumberMustNotBeNull() {
        Configuration instance = new Configuration(null, 30, 50, "sharedTest", "download Test", true, true, ignoreFiles, ignoreFilesStartingWith,ignoreFilesWithExtension);
    }

    @Test(expected = IllegalStateException.class)
    public void testUDPPortNumberMustNonNegative() {
        Configuration instance = new Configuration(-1, 30, 50, "sharedTest", "download Test", true, true, ignoreFiles, ignoreFilesStartingWith,ignoreFilesWithExtension);
    }

    @Test(expected = IllegalStateException.class)
    public void testUDPTimeAnnouncementMustNotBeNull() {
        Configuration instance = new Configuration(80, null, 50, "sharedTest", "download Test", true, true, ignoreFiles, ignoreFilesStartingWith,ignoreFilesWithExtension);
    }

    @Test(expected = IllegalStateException.class)
    public void testUDPTimeAnnouncementMustNonNegative() {
        Configuration instance = new Configuration(80, -1, 50, "sharedTest", "download Test", true, true, ignoreFiles, ignoreFilesStartingWith,ignoreFilesWithExtension);
    }

    @Test(expected = IllegalStateException.class)
    public void testRefreshFileTimeMustNotBeNull() {
        Configuration instance = new Configuration(80, 30, null, "sharedTest", "download Test", true, true, ignoreFiles, ignoreFilesStartingWith,ignoreFilesWithExtension);
    }

    @Test(expected = IllegalStateException.class)
    public void testRefreshFileTimeMustNonNegative() {
        Configuration instance = new Configuration(80, 30, -1, "sharedTest", "download Test", true, true, ignoreFiles, ignoreFilesStartingWith,ignoreFilesWithExtension);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testIgnoreFilesMustNotBeEmpty() {
        Configuration instance = new Configuration(80, 30, -1, "sharedTest", "download Test", true, true, empty, ignoreFilesStartingWith,ignoreFilesWithExtension);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testIgnoreFilesStartingWithMustNotBeEmpty() {
        Configuration instance = new Configuration(80, 30, -1, "sharedTest", "download Test", true, true, ignoreFiles, empty,ignoreFilesWithExtension);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testIgnoreFilesWithExtensionMustNotBeEmpty() {
        Configuration instance = new Configuration(80, 30, -1, "sharedTest", "download Test", true, true, ignoreFiles, ignoreFilesStartingWith,empty);
    }

    @Test
    public void testConfigurationOK() {
        Configuration instance = new Configuration(80, 30, 50, "sharedTest", "downloadTest", true, true, ignoreFiles, ignoreFilesStartingWith,ignoreFilesWithExtension);
    }

}
