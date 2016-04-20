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
import java.util.Scanner;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import driver.JShell;
import commands.*;

/**
 * This is the test class for the Redirect application. Some cases with valid
 * output and some with stdErr are needed, such that it is not necessary to test
 * it for all commands.
 * 
 * @author Ming Yi Chan, Chengyi Zhang, Raghavan Chandrabalan, and Hao Chen
 *
 */
public class RedirectTest {
  JShell shell;
  Scanner mainScanner = new Scanner(System.in);

  /**
   * Set up the file system JShell
   * 
   * @throws Exception
   */
  @Before
  public void setUp() throws Exception {
    shell = JShell.createJShell();
  }

  /**
   * Reset the JShellRef to be null so that the setUp can rebuild a shell
   * 
   * @throws Exception
   */
  @After
  public void tearDown() throws Exception {
    Field field = (shell.getClass()).getDeclaredField("JShellRef");
    field.setAccessible(true);
    field.set(null, null);
  }

  /**
   * Test if redirect works with Cat
   */
  @Test
  public void testCat() {
    // First creat a file with some content inside
    String[] input = {"\" This is file1.\"", ">", "file1"};
    shell.getStdIn().addAll(input);
    String[] lastTwo = shell.checkRedirect();
    Echo.execute(shell);
    Redirect.execute(lastTwo, shell);
    Assert.assertTrue(shell.getStdErr().isEmpty());
    Assert.assertTrue(shell.getStdOut().isEmpty());
    // Then set an input leading to both some stdOut and stdErr
    String[] input2 = {"file1", "file2", ">", "file3"};
    shell.getStdIn().addAll(input2);
    String[] lastTwo2 = shell.checkRedirect();
    Cat.execute(shell);
    // Redirect the stdOut
    Redirect.execute(lastTwo2, shell);
    // Check if the stdErr is not empty and if the stdOut is empty
    Assert.assertTrue(!shell.getStdErr().isEmpty());
    Assert.assertTrue(shell.getStdOut().isEmpty());
    shell.getStdErr().clear();
    // Then check if the stdOut is extracted correctly
    String[] input3 = {"file3"};
    shell.getStdIn().addAll(input3);
    Cat.execute(shell);
    String expect = "This is file1.";
    String actual = shell.getStdOut().toString().trim();
    Assert.assertTrue(actual.equals(expect));
  }

  /**
   * Test if redirect works with Cd
   */
  @Test
  public void testCd() {
    // Since Cd does nothing to stdOut, just check the case causing stdErr
    String[] input = {"folder1", ">", "file1"};
    shell.getStdIn().addAll(input);
    String[] lastTwo = shell.checkRedirect();
    Cd.execute(shell);
    Redirect.execute(lastTwo, shell);
    Assert.assertTrue(!shell.getStdErr().isEmpty());
    Assert.assertTrue(shell.getStdOut().isEmpty());
    // Check if a new file with no content is built.
    Assert.assertTrue(
        !shell.getWorkingDir().getCurrentDirectory().getAllFiles().isEmpty());
    Assert.assertTrue(shell.getWorkingDir().getCurrentDirectory()
        .getFile("file1").getContent().isEmpty());
  }

  /**
   * Test if redirect works with Echo
   */
  @Test
  public void testEcho() {
    // First the case without stdErr
    String[] input = {"\"This is file1.\"", ">", "file1"};
    shell.getStdIn().addAll(input);
    String[] lastTwo = shell.checkRedirect();
    Echo.execute(shell);
    Redirect.execute(lastTwo, shell);
    // There should be no stdErr or stdOut
    Assert.assertTrue(shell.getStdErr().isEmpty());
    Assert.assertTrue(shell.getStdOut().isEmpty());
    shell.getStdIn().add("file1");
    Cat.execute(shell);
    // A fair file is built
    String actual = shell.getStdOut().toString().trim();
    String expect = "This is file1.";
    Assert.assertTrue(actual.equals(expect));

    // Then check the case causing stdErr
    shell.getStdOut().clear();
    shell.getStdErr().clear();
    shell.getStdIn().clear();
    String[] input2 = {"This is file1.", ">", "file1"};
    shell.getStdIn().addAll(input2);
    String[] lastTwo2 = shell.checkRedirect();
    Echo.execute(shell);
    Redirect.execute(lastTwo2, shell);
    Assert.assertTrue(!shell.getStdErr().isEmpty());
    Assert.assertTrue(shell.getStdOut().isEmpty());
    shell.getStdIn().add("file1");
    Cat.execute(shell);
    // The file is covered by blank.
    actual = shell.getStdOut().toString().trim();
    expect = "";
    Assert.assertTrue(actual.equals(expect));
  }

  /**
   * Test if redirect works with Get
   */
  @Test
  public void testGet() {
    // Get has nothing to do with stdOut, just test the case causing stdErr
    String[] input =
        {"http://www.ub.edu/gilcub/SIMPLE/simple.html", ">", "file1"};
    shell.getStdIn().addAll(input);
    String[] lastTwo = shell.checkRedirect();
    Get.execute(shell);
    Redirect.execute(lastTwo, shell);
    Assert.assertTrue(!shell.getStdErr().isEmpty());
    Assert.assertTrue(shell.getStdOut().isEmpty());
    // Check if a new file with no content is built.
    Assert.assertTrue(
        !shell.getWorkingDir().getCurrentDirectory().getAllFiles().isEmpty());
    Assert.assertTrue(shell.getWorkingDir().getCurrentDirectory()
        .getFile("file1").getContent().isEmpty());
  }

  /**
   * Test if redirect works with History
   */
  @Test
  public void testHistory() {
    // First test the case that going well
    shell.getShellHistory().add("first move");
    String[] input = {">", "file1"};
    shell.getStdIn().addAll(input);
    String[] lastTwo = shell.checkRedirect();
    History.execute(shell);
    Redirect.execute(lastTwo, shell);
    // Make sure both stdErr and stdOut are empty
    Assert.assertTrue(shell.getStdErr().isEmpty());
    Assert.assertTrue(shell.getStdOut().isEmpty());
    shell.getStdIn().add("file1");
    Cat.execute(shell);
    // Check if the redirected file is built correctly
    String expect = "1. first move";
    String actual = shell.getStdOut().toString().trim();
    Assert.assertTrue(actual.equals(expect));

    // Then test the case causing error
    shell.getStdOut().clear();
    String[] input2 = {"-1", ">", "file1"};
    shell.getStdIn().addAll(input2);
    String[] lastTwo2 = shell.checkRedirect();
    History.execute(shell);
    Redirect.execute(lastTwo2, shell);
    // Check the status of stdErr and stdOut
    Assert.assertTrue(!shell.getStdErr().isEmpty());
    Assert.assertTrue(shell.getStdOut().isEmpty());
    shell.getStdIn().add("file1");
    Cat.execute(shell);
    // Check if the file is covered
    expect = "";
    actual = shell.getStdOut().toString().trim();
    Assert.assertTrue(actual.equals(expect));
  }

  /**
   * Test if redirect works with Ls
   */
  @Test
  public void testLs() {
    // First test the case that goes well
    String[] input = {">", "file1"};
    shell.getStdIn().addAll(input);
    String[] lastTwo = shell.checkRedirect();
    Ls.execute(shell);
    Redirect.execute(lastTwo, shell);
    // Make sure both stdErr and stdOut are empty
    Assert.assertTrue(shell.getStdErr().isEmpty());
    Assert.assertTrue(shell.getStdOut().isEmpty());
    shell.getStdIn().add("file1");
    Cat.execute(shell);
    // Check if the redirected file is built correctly
    String expect = "";
    String actual = shell.getStdOut().toString().trim();
    Assert.assertTrue(actual.equals(expect));

    // Then test the case causing error
    shell.getStdOut().clear();
    String[] input2 = {"-1", ">", "file1"};
    shell.getStdIn().addAll(input2);
    String[] lastTwo2 = shell.checkRedirect();
    History.execute(shell);
    Redirect.execute(lastTwo2, shell);
    // Check the status of stdErr and stdOut
    Assert.assertTrue(!shell.getStdErr().isEmpty());
    Assert.assertTrue(shell.getStdOut().isEmpty());
    shell.getStdIn().add("file1");
    Cat.execute(shell);
    // Check if the file is covered
    expect = "";
    actual = shell.getStdOut().toString().trim();
    Assert.assertTrue(actual.equals(expect));
  }

  /**
   * Test if redirect works with Man
   */
  @Test
  public void testMan() {
    // First test the case that goes well
    String[] input = {"man", ">", "file1"};
    shell.getStdIn().addAll(input);
    String[] lastTwo = shell.checkRedirect();
    Man.execute(shell);
    Redirect.execute(lastTwo, shell);
    // Make sure both stdErr and stdOut are empty
    Assert.assertTrue(shell.getStdErr().isEmpty());
    Assert.assertTrue(shell.getStdOut().isEmpty());
    shell.getStdIn().add("file1");
    Cat.execute(shell);
    // Check if the redirected file is built correctly
    String expect = Man.describeCmd();
    String actual = shell.getStdOut().toString().trim();
    Assert.assertTrue(actual.equals(expect));

    // Then test the case causing error
    shell.getStdOut().clear();
    String[] input2 = {"-1", ">", "file1"};
    shell.getStdIn().addAll(input2);
    String[] lastTwo2 = shell.checkRedirect();
    Man.execute(shell);
    Redirect.execute(lastTwo2, shell);
    // Check the status of stdErr and stdOut
    Assert.assertTrue(!shell.getStdErr().isEmpty());
    Assert.assertTrue(shell.getStdOut().isEmpty());
    shell.getStdIn().add("file1");
    Cat.execute(shell);
    // Check if the file is covered
    expect = "";
    actual = shell.getStdOut().toString().trim();
    Assert.assertTrue(actual.equals(expect));
  }

  /**
   * Test if redirect works with Mkdir
   */
  @Test
  public void testMkdir() {
    // Mkdir has nothing to do with stdOut, just test the case causing stdErr
    String[] input = {"folder1/folder2", ">>", "file1"};
    shell.getStdIn().addAll(input);
    String[] lastTwo = shell.checkRedirect();
    Mkdir.execute(shell);
    Redirect.execute(lastTwo, shell);
    Assert.assertTrue(!shell.getStdErr().isEmpty());
    Assert.assertTrue(shell.getStdOut().isEmpty());
    // Check if a new file with no content is built.
    Assert.assertTrue(
        !shell.getWorkingDir().getCurrentDirectory().getAllFiles().isEmpty());
    Assert.assertTrue(shell.getWorkingDir().getCurrentDirectory()
        .getFile("file1").getContent().isEmpty());
  }

  /**
   * Test if redirect works with Popd
   */
  @Test
  public void testPopd() {
    // Popd has nothing to do with stdOut, just test the case causing stdErr
    String[] input = {">>", "file1"};
    shell.getStdIn().addAll(input);
    String[] lastTwo = shell.checkRedirect();
    Popd.execute(shell);
    Redirect.execute(lastTwo, shell);
    Assert.assertTrue(!shell.getStdErr().isEmpty());
    Assert.assertTrue(shell.getStdOut().isEmpty());
    // Check if a new file with no content is built.
    Assert.assertTrue(
        !shell.getWorkingDir().getCurrentDirectory().getAllFiles().isEmpty());
    Assert.assertTrue(shell.getWorkingDir().getCurrentDirectory()
        .getFile("file1").getContent().isEmpty());
  }

  /**
   * Test if redirect works with Pushd
   */
  @Test
  public void testPushd() {
    // Pushd has nothing to do with stdOut, just test the case causing stdErr
    String[] input = {">>", "file1"};
    shell.getStdIn().addAll(input);
    String[] lastTwo = shell.checkRedirect();
    Pushd.execute(shell);
    Redirect.execute(lastTwo, shell);
    Assert.assertTrue(!shell.getStdErr().isEmpty());
    Assert.assertTrue(shell.getStdOut().isEmpty());
    // Check if a new file with no content is built.
    Assert.assertTrue(
        !shell.getWorkingDir().getCurrentDirectory().getAllFiles().isEmpty());
    Assert.assertTrue(shell.getWorkingDir().getCurrentDirectory()
        .getFile("file1").getContent().isEmpty());
  }

  /**
   * Test if redirect works with Pwd
   */
  @Test
  public void testPwd() {
    // First test the case that goes well
    String[] input = {">", "file1"};
    shell.getStdIn().addAll(input);
    String[] lastTwo = shell.checkRedirect();
    Pwd.execute(shell);
    Redirect.execute(lastTwo, shell);
    // Make sure both stdErr and stdOut are empty
    Assert.assertTrue(shell.getStdErr().isEmpty());
    Assert.assertTrue(shell.getStdOut().isEmpty());
    shell.getStdIn().add("file1");
    Cat.execute(shell);
    // Check if the redirected file is built correctly
    String expect = "/";
    String actual = shell.getStdOut().toString().trim();
    Assert.assertTrue(actual.equals(expect));

    // Then test the case causing error
    shell.getStdOut().clear();
    String[] input2 = {"-1", ">>", "file1"};
    shell.getStdIn().addAll(input2);
    String[] lastTwo2 = shell.checkRedirect();
    Pwd.execute(shell);
    Redirect.execute(lastTwo2, shell);
    // Check the status of stdErr and stdOut
    Assert.assertTrue(!shell.getStdErr().isEmpty());
    Assert.assertTrue(shell.getStdOut().isEmpty());
    shell.getStdIn().add("file1");
    Cat.execute(shell);
    // Check if the file is appended rather than covered
    expect = "/";
    actual = shell.getStdOut().toString().trim();
    Assert.assertTrue(actual.equals(expect));
  }

}
