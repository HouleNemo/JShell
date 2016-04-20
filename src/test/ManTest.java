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
import java.lang.reflect.InvocationTargetException;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import commands.Commands;
import driver.JShell;

public class ManTest {
  JShell shell = JShell.createJShell();

  @After
  public void tearDown() throws Exception {
    Field field = (shell.getClass()).getDeclaredField("JShellRef");
    field.setAccessible(true);
    field.set(null, null);
  }

  /**
   * Test the basic conditions where man works.
   */
  @Test
  public void testCmd() {
    String[] cmds = Commands.getCmds();
    // For all valid commands, check if man returns the same content as their
    // describeCmd method.
    for (String cmd : cmds) {
      shell.getStdIn().add(cmd);
      commands.Man.execute(shell);
      Class<?> callingCmd;
      String actual = "";
      try {
        cmd = cmd.substring(0, 1).toUpperCase() + cmd.substring(1);
        callingCmd = Class.forName("commands." + cmd);
        actual = (String) callingCmd.getMethod("describeCmd")
            .invoke((Object[]) null, (Object[]) null);
      } catch (IllegalAccessException | IllegalArgumentException
          | InvocationTargetException | NoSuchMethodException
          | SecurityException e) {
        e.printStackTrace();
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
      }
      String expect = shell.getStdOut().poll();
      Assert.assertTrue(expect.equals(actual));
    }
  }

  /**
   * Test if man returns error and no output if the input is invalid.
   */
  @Test
  public void testInvalidCmd() {
    String[] invalidCmds = {"test1", "test2"};
    for (String invalidCmd : invalidCmds) {
      shell.getStdIn().add(invalidCmd);
      commands.Man.execute(shell);
      Assert.assertTrue(shell.getStdOut().peek() == null);
      Assert.assertFalse(shell.getStdErr().peek() == null);
    }
  }

  /**
   * Test if man returns error and no output if the input is more than one
   * parameter.
   */
  @Test
  public void testMoreParam() {
    String[] input = {"man", "echo"};
    shell.getStdIn().addAll(input);
    commands.Man.execute(shell);
    Assert.assertTrue(shell.getStdOut().peek() == null);
    Assert.assertFalse(shell.getStdErr().peek() == null);
  }

}
