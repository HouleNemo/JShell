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

import org.junit.*;

import commands.*;
import driver.*;

public class EchoTest {

  private JShell shell;
  private Directory exp;

  /**
   * Sets up new JShell for each test case
   */

  @Before
  public void setUp() {
    shell = JShell.createJShell();
    exp = new Directory("expectBase");
  }

  @After
  public void tearDown() throws Exception {
    Field field = (shell.getClass()).getDeclaredField("JShellRef");
    field.setAccessible(true);
    field.set(null, null);
  }

  /**
   * Test case where no argument is provided.
   */
  @Test
  public void testMissingArgument() {
    // Execute echo
    Echo.execute(shell);

    // Output and error stream
    String actualOut = shell.getStdOut().poll();
    String actualErr = shell.getStdErr().poll();

    // Expected output and error stream
    String expectedOut = null;
    String expectedErr = "Missing STRING. Enter \"man echo\" to view the "
        + "manual for the echo command.";

    // Check expected equals actual
    Assert.assertEquals(expectedOut, actualOut);
    Assert.assertEquals(expectedErr, actualErr);
  }

  /**
   * Test case where no string is provided.
   */
  @Test
  public void testMissingString() {
    // Set input
    shell.getStdIn().add("");

    // Execute echo
    Echo.execute(shell);

    // Output and error stream
    String actualOut = shell.getStdOut().poll();
    String actualErr = shell.getStdErr().poll();

    // Expected output and error stream
    String expectedOut = null;
    String expectedErr = "The input string needs to be surrounded by double "
        + "quotation marks.";

    // Check expected equals actual
    Assert.assertEquals(expectedOut, actualOut);
    Assert.assertEquals(expectedErr, actualErr);
  }

  /**
   * Test case where empty string is provided.
   */
  @Test
  public void testEmptyString() {
    // Set input
    shell.getStdIn().add("\"\"");

    // Execute echo
    Echo.execute(shell);

    // Output and error stream
    String actualOut = shell.getStdOut().poll();
    String actualErr = shell.getStdErr().poll();

    // Expected output and error stream
    String expectedOut = "";
    String expectedErr = null;

    // Check expected equals actual
    Assert.assertEquals(expectedOut, actualOut);
    Assert.assertEquals(expectedErr, actualErr);
  }

  /**
   * Test case where a random string is provided.
   */
  @Test
  public void testString() {
    // Set input
    shell.getStdIn().add("\"a random string\"");

    // Execute echo
    Echo.execute(shell);

    // Output and error stream
    String actualOut = shell.getStdOut().poll();
    String actualErr = shell.getStdErr().poll();

    // Expected output and error stream
    String expectedOut = "a random string";
    String expectedErr = null;

    // Check expected equals actual
    Assert.assertEquals(expectedOut, actualOut);
    Assert.assertEquals(expectedErr, actualErr);
  }

}
