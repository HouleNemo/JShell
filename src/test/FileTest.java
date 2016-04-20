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

public class FileTest {

  private Directory root;
  private Directory dir1;
  private File file1;
  private File file2;
  private File file3;

  @Before
  public void setUp() {
    root = new Directory("/");
    dir1 = new Directory("dir1", root);
    file1 = new File("file1", root);
    file2 = new File("file2", root);
    file3 = new File("file3", dir1);
  }

  /**
   * Tests whether a file's content can be erased.
   */
  @Test
  public void eraseTest() {
    file1.append("Erase this line.");
    assertEquals(file1.getContent(), "Erase this line.");
    file1.erase();
    assertEquals(file1.getContent(), "");
  }

  /**
   * Tests whether a file's content can be appended.
   */
  @Test
  public void appendTest() {
    file1.append("Check if this line was appended.");
    assertEquals(file1.getContent(), "Check if this line was appended.");
    file1.append(" Well, was it?");
    assertEquals(file1.getContent(),
        "Check if this line was appended. Well, was it?");
  }

  /**
   * Tests that the getName function returns the file name.
   */
  @Test
  public void getNameTest() {
    assertEquals(file1.getName(), "file1");
  }

  /**
   * Tests that the getParent function returns the proper parent directory.
   */
  @Test
  public void getParentTest() {
    assertEquals(file3.getParent(), dir1);
    assertEquals(file2.getParent(), root);
  }

  /**
   * Tests that the getContent function returns the file contents.
   */
  @Test
  public void getContent() {
    file3.append("Blah blah blah.");
    assertEquals(file3.getContent(), "Blah blah blah.");
  }

}
