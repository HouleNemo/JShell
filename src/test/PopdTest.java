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

import commands.Popd;
import commands.Pushd;
import driver.*;

public class PopdTest {

  JShell shell;

  /**
   * Setup for PopdTest Creates a shell containing a root directory and two
   * child directories.
   */
  @Before
  public void setUp() throws Exception {
    shell = JShell.createJShell();
    shell.getDirectoryStack()
        .push(new WorkingDir(shell.getWorkingDir().getCurrentDirectory()));
    shell.getWorkingDir().getCurrentDirectory().addDir(
        new Directory("a", shell.getWorkingDir().getCurrentDirectory()));
    shell.getWorkingDir().getCurrentDirectory().addDir(
        new Directory("b", shell.getWorkingDir().getCurrentDirectory()));
    shell.getWorkingDir().changeToChild("a");
    shell.getDirectoryStack()
        .push(new WorkingDir(shell.getWorkingDir().getCurrentDirectory()));
    shell.getWorkingDir().changeToParent();
    shell.getWorkingDir().changeToChild("b");
  }

  @After
  public void tearDown() throws Exception {
    Field field = (shell.getClass()).getDeclaredField("JShellRef");
    field.setAccessible(true);
    field.set(null, null);
  }
  
  /**
   * Tests the execute function in popd.
   */
  @Test
  public void testExecute() {
    Popd.execute(shell);
    assertTrue("Pop was unsuccessful.",
        shell.getWorkingDir().getCurrentDirectory().getName() == "a");
    Popd.execute(shell);
    assertTrue("Pop was unsuccessful.",
        shell.getWorkingDir().getCurrentDirectory().getName() == "/");
    Popd.execute(shell);
    assertTrue("Error message not sent.",
        shell.getStdErr().poll().equals("Stack is empty."));
  }

  /**
   * Checks to see whether the proper errors are given.
   */
  public void testErrorMessages() {
    Popd.execute(shell);
    assertTrue("Error message not sent.",
        shell.getStdErr().poll().equals("Stack is empty."));
    shell.getStdIn().add("pppp");
    Pushd.execute(shell);
    assertTrue(shell.getStdErr().poll().equals("Too many parameters."));
  }

}
