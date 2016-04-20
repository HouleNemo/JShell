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
import org.junit.Before;
import org.junit.Test;

import commands.Cd;
import driver.Directory;
import driver.JShell;

public class CdTest {

  JShell shell;

  /**
   * Create a JShell object to test Cd with.
   */
  @Before
  public void setUp() throws Exception {
    shell = JShell.createJShell();
    shell.getWorkingDir().getCurrentDirectory().addDir(
        new Directory("a", shell.getWorkingDir().getCurrentDirectory()));
    shell.getWorkingDir().getCurrentDirectory().addDir(
        new Directory("b", shell.getWorkingDir().getCurrentDirectory()));
    shell.getWorkingDir().changeToChild("a");
    shell.getWorkingDir().getCurrentDirectory().addDir(
        new Directory("c", shell.getWorkingDir().getCurrentDirectory()));
    shell.getWorkingDir().getCurrentDirectory().addDir(
        new Directory("d", shell.getWorkingDir().getCurrentDirectory()));
  }
  
  @After
  public void tearDown() throws Exception {
    Field field = (shell.getClass()).getDeclaredField("JShellRef");
    field.setAccessible(true);
    field.set(null, null);
  }

  /**
   * Tests the execute function with general cases.
   */
  @Test
  public void testExecute() {
    shell.getStdIn().add("/");
    Cd.execute(shell);
    assertTrue(
        shell.getWorkingDir().getCurrentDirectory().getName().equals("/"));
    shell.getStdIn().add("a");
    Cd.execute(shell);
    assertTrue(
        shell.getWorkingDir().getCurrentDirectory().getName().equals("a"));
    shell.getStdIn().add("../a/./../b");
    Cd.execute(shell);
    assertTrue(
        shell.getWorkingDir().getCurrentDirectory().getName().equals("b"));
    shell.getStdIn().add("/a/d");
    Cd.execute(shell);
    assertTrue(
        shell.getWorkingDir().getCurrentDirectory().getName().equals("d"));
    shell.getStdIn().add("../../b/e/f/g/h/j//fd/sad/e//");
    Cd.execute(shell);
    assertTrue("Invalid directory change was not reverted.",
        shell.getWorkingDir().getCurrentDirectory().getName().equals("d"));
  }

  /**
   * Checks to see whether the proper errors are given.
   */
  public void testErrorMessages() {
    shell.getStdIn().add("../../b/e/f/g/h/j//fd/sad/e//");
    Cd.execute(shell);
    assertTrue("Incorrect error message.", shell.getStdErr().poll()
        .equals("Directory with the given path does not exist."));
    shell.getStdIn().add("Ha");
    shell.getStdIn().add("ha");
    Cd.execute(shell);
    assertTrue("Incorrect error message.",
        shell.getStdErr().poll().equals("Too many parameters."));
    Cd.execute(shell);
    assertTrue("Incorrect error message.",
        shell.getStdErr().poll().equals("Parameter missing."));
  }

}
