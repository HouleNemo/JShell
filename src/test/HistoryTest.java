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

import java.lang.reflect.Field;
import java.util.Arrays;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import driver.JShell;

public class HistoryTest {
  private JShell shell = JShell.createJShell();

  @Before
  public void setUp() {
    // Use add method to implement to history list.
    shell.getShellHistory().add("mkdir folder1");
    shell.getShellHistory().add("mkdir folder1/folder2");
    shell.getShellHistory().add("cd folder1");
    shell.getShellHistory().add("echo \"hi\" file1");
    shell.getShellHistory().add("history");
  }

  @After
  public void tearDown() throws Exception {
    Field field = (shell.getClass()).getDeclaredField("JShellRef");
    field.setAccessible(true);
    field.set(null, null);
  }

  /**
   * Test if History works without param
   */
  @Test
  public void testNoParam() {
    commands.History.execute(shell);
    String act = shell.getStdOut().poll();
    String[] actual = act.split("\n");
    String[] expect = {"1. mkdir folder1", "2. mkdir folder1/folder2",
        "3. cd folder1", "4. echo \"hi\" file1", "5. history"};
    Assert.assertTrue(Arrays.equals(expect, actual));
  }

  /**
   * Test if History returns the recent operation.
   */
  @Test
  public void testRecent() {
    String[] input = {"3"};
    shell.getStdIn().addAll(input);
    commands.History.execute(shell);
    String act = shell.getStdOut().poll();
    String[] actual = act.split("\n");
    String[] expect = {"3. cd folder1", "4. echo \"hi\" file1", "5. history"};
    Assert.assertTrue(Arrays.equals(expect, actual));
  }

  /**
   * Test if History returns an error when the input is invalid.
   */
  @Test
  public void testInvalidParam() {
    String[] input = {"-1"};
    shell.getStdIn().addAll(input);
    commands.History.execute(shell);
    Assert.assertTrue(
        shell.getStdOut().isEmpty() && !shell.getStdErr().isEmpty());
  }

  /**
   * Test if History returns an error when the input is too many.
   */
  @Test
  public void testTooManyParam() {
    String[] input = {"2", "3"};
    shell.getStdIn().addAll(input);
    commands.History.execute(shell);
    Assert.assertTrue(
        shell.getStdOut().isEmpty() && !shell.getStdErr().isEmpty());
  }

  /**
   * Test if History include invalid operation.
   */
  @Test
  public void testExistWrongInput() {
    String[] input = {"2", "3"};
    shell.getStdIn().addAll(input);
    commands.History.execute(shell);
    shell.getStdIn().clear();
    shell.getStdErr().clear();
    shell.getShellHistory().add("history 2 3");
    shell.getShellHistory().add("history");
    commands.History.execute(shell);
    String act = shell.getStdOut().poll();
    String[] actual = act.split("\n");
    for (String a : actual) {
      System.out.println("actual" + a);
    }
    String[] expect = {"1. mkdir folder1", "2. mkdir folder1/folder2",
        "3. cd folder1", "4. echo \"hi\" file1", "5. history", "6. history 2 3",
        "7. history"};
    Assert.assertTrue(Arrays.equals(expect, actual));
  }
}
