// **********************************************************
// Assignment2:
// Student1:
// CDF user_name: c6chenha
// UT Student #: 1001551741
// Author: Hao Chen
//
// Student2:
// CDF user_name: c5chandt
// UT Student #: 1002429972
// Author: Raghavan Chandrabalan
//
// Student3:
// CDF user_name: c5zhangd
// UT Student #: 1001169610
// Author: Chengyi Zhang
//
// Student4:
// CDF user_name: c5chanmi
// UT Student #: 1000647473
// Author: Ming Yi Chan
//
//
// Honor Code: I pledge that this program represents my own
// program code and that I have coded on my own. I received
// help from no one in designing and debugging my program.
// I have also read the plagiarism section in the course info
// sheet of CSC 207 and understand the consequences.
// *********************************************************
package test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import driver.*;
import java.util.*;

public class DirectoryTest {

  private Directory root;
  private Directory dir1;
  private Directory dir1a;
  private Directory dir2;
  private Directory dir3;
  private File file1;
  private File file2;
  private File file3;

  @Before
  public void setUp() {
    root = new Directory("/");
    dir1 = new Directory("dir1", root);
    dir1a = new Directory("dir1a", dir1);
    dir2 = new Directory("dir2", root);
    dir3 = new Directory("dir3", root);
    file1 = new File("file1", root);
    file2 = new File("file2", root);
    file3 = new File("file3", root);

  }

  /**
   * Tests the ability to add a directory to a directory.
   */
  @Test
  public void addDirTest() {
    dir2.addDir(dir1a);
    assertEquals(dir1a, dir2.getChild("dir1a"));
  }

  /**
   * Tests the ability to get a file from a directory based on its name.
   */
  @Test
  public void getFileTest() {
    assertEquals(file1, root.getFile("file1"));
    assertEquals(null, root.getFile("Blablabla"));
  }

  /**
   * Tests the ability to get a directory from a directory based on its name.
   */
  @Test
  public void getChildTest() {
    assertEquals(dir1, root.getChild("dir1"));
    assertEquals(null, root.getChild("Blablabla"));
  }

  /**
   * Tests the getParent function to make sure the right parent is returned.
   */
  @Test
  public void getParentTest() {
    assertEquals(dir1, dir1a.getParent());
    assertEquals(root, root.getParent());
    assertNotEquals(root, dir1a.getParent());
  }

  /**
   * Tests the getName function for the name of the directory.
   */
  @Test
  public void getNameTest() {
    assertEquals("/", root.getName());
    assertNotEquals("This is not the same name.", root.getName());
  }

  /**
   * Tests the getPath function for the full path of the directory.
   */
  @Test
  public void getPathTest() {
    assertEquals("/", root.getPath());
    assertEquals("/dir1/dir1a", dir1a.getPath());
    assertEquals("/dir2", dir2.getPath());
  }

  /**
   * Tests the getAllChildDirs function.
   */
  @Test
  public void getAllChildDirsTest() {

    HashMap<String, Directory> compare = new HashMap<String, Directory>();
    compare.put("dir1", dir1);
    compare.put("dir2", dir2);
    compare.put("dir3", dir3);

    assertTrue(root.getAllChildDirs().equals(compare));

    compare.remove("dir1");

    assertFalse(root.getAllChildDirs().equals(compare));
  }

  /**
   * Tests the getAllFiles function.
   */
  @Test
  public void getAllFilesTest() {
    HashMap<String, File> compare = new HashMap<String, File>();
    compare.put("file1", file1);
    compare.put("file2", file2);
    compare.put("file3", file3);

    assertTrue(root.getAllFiles().equals(compare));

    compare.remove("file3");

    assertFalse(root.getAllFiles().equals(compare));
  }

  /**
   * Tests the getAllChildDirsString function.
   */
  @Test
  public void getAllChildDirsStringTest() {
    assertTrue(Arrays.asList(root.getAllChildDirsString(" ").split(" "))
        .contains("dir1"));
    assertTrue(Arrays.asList(root.getAllChildDirsString(" ").split(" "))
        .contains("dir2"));
    assertTrue(Arrays.asList(root.getAllChildDirsString(" ").split(" "))
        .contains("dir3"));
    assertFalse(Arrays.asList(root.getAllChildDirsString(" ").split(" "))
        .contains("dir4"));
  }

  /**
   * Tests the getAllFilesString function.
   */
  @Test
  public void getAllFilesStringTest() {
    assertTrue(Arrays.asList(root.getAllFilesString(" ").split(" "))
        .contains("file1"));
    assertTrue(Arrays.asList(root.getAllFilesString(" ").split(" "))
        .contains("file2"));
    assertTrue(Arrays.asList(root.getAllFilesString(" ").split(" "))
        .contains("file3"));
    assertFalse(Arrays.asList(root.getAllFilesString(" ").split(" "))
        .contains("file4"));
  }

  /**
   * Tests the getAllChildDirsNames function.
   */
  @Test
  public void getAllChildDirsNamesTest() {
    String[] names = root.getAllChildDirNames();
    String[] compare = {"dir1", "dir2", "dir3"};

    for (int i = 0; i < compare.length; i++) {
      assertTrue(Arrays.asList(names).contains(compare[i]));
    }
  }

  /**
   * Tests the getAllFileNames function.
   */
  @Test
  public void getAllFileNamesTest() {

    String[] names = root.getAllFileNames();
    String[] compare = {"file1", "file2", "file3"};

    for (int i = 0; i < compare.length; i++) {
      assertTrue(Arrays.asList(names).contains(compare[i]));
    }
  }

  /**
   * Tests the isValidName function.
   */
  @Test
  public void isValidNameTest() {
    assertFalse(root.isValidName(""));
    assertFalse(root.isValidName(".."));
    assertFalse(root.isValidName("."));
    assertFalse(root.isValidName("dir1"));
  }

}
