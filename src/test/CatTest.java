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

import commands.Cat;
import commands.Cd;
import commands.Echo;
import driver.*;
import driver.JShell;

public class CatTest {

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
    shell.getWorkingDir().getCurrentDirectory().addFile(
        new File("rootFile", shell.getWorkingDir().getCurrentDirectory()));
    shell.getWorkingDir().getCurrentDirectory().getFile("rootFile")
        .append("rootTest");
    shell.getWorkingDir().changeToChild("a");
    shell.getWorkingDir().getCurrentDirectory().addFile(
        new File("aFile", shell.getWorkingDir().getCurrentDirectory()));
    shell.getWorkingDir().getCurrentDirectory().getFile("aFile")
        .append("aTest");
    shell.getWorkingDir().getCurrentDirectory().addDir(
        new Directory("c", shell.getWorkingDir().getCurrentDirectory()));
    shell.getWorkingDir().getCurrentDirectory().addDir(
        new Directory("d", shell.getWorkingDir().getCurrentDirectory()));
    shell.getWorkingDir().changeToChild("d");
    shell.getWorkingDir().getCurrentDirectory().addFile(
        new File("dFile", shell.getWorkingDir().getCurrentDirectory()));
    shell.getWorkingDir().getCurrentDirectory().getFile("dFile")
        .append("dTest");
  }

  @After
  public void tearDown() throws Exception {
    Field field = (shell.getClass()).getDeclaredField("JShellRef");
    field.setAccessible(true);
    field.set(null, null);
  }
  
  /**
   * Tests some general cases for the execute function.
   */
  @Test
  public void testExecute() {
    shell.getStdIn().add("dFile");
    Cat.execute(shell);
    assertTrue(shell.getStdOut().poll().equals("dTest\n"));
    shell.getStdIn().add("../aFile");
    Cat.execute(shell);
    assertTrue(shell.getStdOut().poll().equals("aTest\n"));
    shell.getStdIn().add("/rootFile");
    Cat.execute(shell);
    assertTrue(shell.getStdOut().poll().equals("rootTest\n"));
    shell.getStdIn().add("/a/aFile");
    shell.getStdIn().add("../../rootFile");
    shell.getStdIn().add("dFile");
    Cat.execute(shell);
    assertTrue(shell.getStdOut().poll().equals("aTest\n"));
    assertTrue(shell.getStdOut().poll().equals("\n\n\n"));
    assertTrue(shell.getStdOut().poll().equals("rootTest\n"));
    assertTrue(shell.getStdOut().poll().equals("\n\n\n"));
    assertTrue(shell.getStdOut().poll().equals("dTest\n"));
  }

  /**
   * Tests inputs with multiple files (A2b requirement)
   */
  @Test
  public void testMultiFiles() {
    String[] input = {"dFile", "/a/aFile", "invalidFile"};
    shell.getStdIn().addAll(input);
    Cat.execute(shell);
    assertTrue(shell.getStdOut().poll().equals("dTest\n"));
    assertTrue(shell.getStdOut().poll().equals("\n\n\n"));
    assertTrue(shell.getStdOut().poll().equals("aTest\n"));
    assertTrue(shell.getStdOut().poll().equals("\n\n\n"));
    assertTrue(shell.getStdErr().poll()
        .equals("No file with the name invalidFile exists.\n"));
  }

  /**
   * Checks to see whether the proper errors are given.
   */
  @Test
  public void testErrorMessages() {
    shell.getStdIn().add("../../b/e/f/g/h/j//fd/sad/e//");
    Cat.execute(shell);
    assertTrue("Incorrect error message.", shell.getStdErr().poll()
        .equals("Invalid path in ../../b/e/f/g/h/j//fd/sad/e//"));
    shell.getStdIn().add("44444");
    Cat.execute(shell);
    assertTrue("Incorrect error message.", shell.getStdErr().poll()
        .equals("No file with the name 44444 exists.\n"));
    Cat.execute(shell);
    assertTrue("Incorrect error message.",
        shell.getStdErr().poll().equals("No file name provided."));
  }
}
