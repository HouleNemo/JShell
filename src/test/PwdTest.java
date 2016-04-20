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

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import driver.JShell;

public class PwdTest {
  private JShell shell = JShell.createJShell();

  /**
   * Setup the file system. Creat a folder with one subfolder in it, and the
   * subfolder also has a child folder inside. Then change directory to the leaf
   * folder.
   */
  @Before
  public void setUp() {
    String[] input1 = {"f1", "f1/f2", "f1/f2/f3"};
    shell.getStdIn().addAll(input1);
    commands.Mkdir.execute(shell);
    String[] input2 = {"/f1/f2/f3"};
    shell.getStdIn().addAll(input2);
    commands.Cd.execute(shell);
  }

  @After
  public void tearDown() throws Exception {
    Field field = (shell.getClass()).getDeclaredField("JShellRef");
    field.setAccessible(true);
    field.set(null, null);
  }

  /**
   * Test the basic condition of Pwd.
   */
  @Test
  public void testBasic() {
    commands.Pwd.execute(shell);
    String expect = "/f1/f2/f3";
    String actual = shell.getStdOut().toString().trim();
    System.out.println(actual);
    Assert.assertEquals(expect, actual);
  }

  /**
   * Test if Pwd returns an error when there is unnecessary parameter.
   */
  @Test
  public void testUnnecessaryParam() {
    String[] input = {"f1"};
    shell.getStdIn().addAll(input);
    commands.Pwd.execute(shell);
    String actual = shell.getStdOut().toString().trim();
    Assert.assertTrue(
        (actual.isEmpty() && !shell.getStdErr().toString().isEmpty()));
  }

}
