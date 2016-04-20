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
import driver.*;
import commands.*;

public class RedoTest {
  
  private JShell shell;
  
  /**
   * File system fixture for testing ! command.
   */
  @Before
  public void setUp(){
    shell = JShell.createJShell();
    shell.getShellHistory().clear();
    shell.getWorkingDir().getCurrentDirectory().addDir(
        new Directory("dir1", shell.getWorkingDir().getCurrentDirectory()));
    shell.getShellHistory().add("mkdir dir1");
    shell.getShellHistory().add("pwd");
    shell.getShellHistory().add("this is not a command");
    shell.getShellHistory().add("!2");
    shell.getShellHistory().add("cd dir1");
    shell.getShellHistory().add("ls");
    shell.getShellHistory().add("man echo");
    shell.getShellHistory().add("!9");
    shell.getShellHistory().add("!8");
    shell.getShellHistory().add("!10");
    shell.getStdIn().clear();
    shell.getStdOut().clear();
    shell.getStdErr().clear();
  }
  
  @After
  public void tearDown() throws Exception {
    Field field = (shell.getClass()).getDeclaredField("JShellRef");
    field.setAccessible(true);
    field.set(null, null);
  }
  
  /**
   * Tests ! where the recalled command is mkdir.
   */
  @Test
  public void executeRedoMkdirTest() {
    shell.setCmd("!1");
    Redo.execute(shell);
    String expected = "dir1 is already being used in the current directory."
        + " Please use a\ndifferent name.\n";
    assertEquals(expected, shell.getStdErr().toString());
  }
  
  @Test
  public void executeRedoPwdTest() {
    shell.setCmd("!2");
    Redo.execute(shell);
    String expected = "/\n";
    assertEquals(expected, shell.getStdOut().toString());
  }
  
  /**
   * Tests ! where the recalled command is not valid.
   */
  @Test
  public void executeRedoInvalidCommandTest() {
    shell.setCmd("!3");
    Redo.execute(shell);
    String expected = "You tried to execute an invalid command. Please try again."
        + "Type \"history\" to see a list of past commands.";
    assertEquals(expected, shell.getStdErr().toString().trim());
  }
  
  /**
   * Tests ! where the recalled command is another !.
   */
  @Test
  public void executeRedoRedoTest(){
    shell.setCmd("!4");
    Redo.execute(shell);
    String expected = "/\n";
    assertEquals(expected, shell.getStdOut().toString());
  }
  
  /**
   * Tests ! where the recalled command is cd.
   */
  @Test
  public void executeRedoCdTest() {
    shell.setCmd("!5");
    Redo.execute(shell);
    String expected = "dir1";
    assertEquals(expected, shell.getWorkingDir().getCurrentDirectory()
        .getName());
  }
  
  /**
   * Tests if ! prints the appropriate error message for an input integer
   * greater than or equal to the history log size.
   */
  @Test
  public void executeRedoExceedingHistoryBoundsErrorTest() {
    shell.setCmd("!10");
    Redo.execute(shell);
    String expected = "Parameter exceeds bounds of history log.\n";
    assertEquals(expected, shell.getStdErr().toString());
  }
  
  /**
   * Tests if ! prints the appropriate error message for an input that is not a
   * positive integer.
   */
  @Test
  public void executeRedoNotPositiveIntegerErrorTest() {
    shell.setCmd("!-2");
    Redo.execute(shell);
    String expected = "Parameter must be a positive integer.\n";
    assertEquals(expected, shell.getStdErr().toString());
  }
  
  /**
   * Tests if ! prints the appropriate error message for too many input
   * parameters entered.
   */
  @Test
  public void executeRedoTooManyParametersErrorTest() {
    shell.setCmd("!3");
    shell.getStdIn().add("4");
    Redo.execute(shell);
    String expected = "Too many parameters.\n";
    assertEquals(expected, shell.getStdErr().toString());
  }
  
  /**
   * Tests if ! prints the appropriate error message when redo enters an
   * infinite loop.
   */
  @Test
  public void executeRedoInfiniteLoopErrorTest() {
    shell.setCmd("!9");
    Redo.execute(shell);
    String expected = "You are building an infinite loop!\n";
    assertEquals(expected, shell.getStdErr().toString());
  }
}
