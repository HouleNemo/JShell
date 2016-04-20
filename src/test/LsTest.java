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
import org.junit.Test;

import commands.Cd;
import commands.Ls;

import org.junit.Before;
import driver.*;

public class LsTest {
  
  private JShell shell;
  
  /**
   * File system fixture for LsTest.
   */
  @Before
  public void setUp(){
    shell = JShell.createJShell();
    shell.getWorkingDir().getCurrentDirectory().addDir(
        new Directory("dir1", shell.getWorkingDir().getCurrentDirectory()));
    shell.getWorkingDir().getCurrentDirectory().addDir(
        new Directory("dir2", shell.getWorkingDir().getCurrentDirectory()));
    shell.getWorkingDir().getCurrentDirectory().addFile(
        new File("rootFile", shell.getWorkingDir().getCurrentDirectory()));
    shell.getWorkingDir().getCurrentDirectory().getFile("rootFile")
        .append("rootTest");
    shell.getWorkingDir().changeToChild("dir1");
    shell.getWorkingDir().getCurrentDirectory().addFile(
        new File("dir1File1", shell.getWorkingDir().getCurrentDirectory()));
    shell.getWorkingDir().getCurrentDirectory().getFile("dir1File1")
        .append("dir1Test1");
    shell.getWorkingDir().getCurrentDirectory().addFile(
        new File("dir1File2", shell.getWorkingDir().getCurrentDirectory()));
    shell.getWorkingDir().getCurrentDirectory().getFile("dir1File2")
        .append("dir1Test2");
    shell.getWorkingDir().getCurrentDirectory().addDir(
        new Directory("dir11", shell.getWorkingDir().getCurrentDirectory()));
    shell.getWorkingDir().getCurrentDirectory().addDir(
        new Directory("dir12", shell.getWorkingDir().getCurrentDirectory()));
    shell.getWorkingDir().changeToChild("dir12");
    shell.getWorkingDir().getCurrentDirectory().addFile(
        new File("dir12File", shell.getWorkingDir().getCurrentDirectory()));
    shell.getWorkingDir().getCurrentDirectory().getFile("dir12File")
        .append("dir12Test");
    shell.getStdIn().add("/");
    Cd.execute(shell);
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
   * Tests ls with -R without additional arguments.
   */
  @Test
  public void executeRecursiveNoPathTest(){
    shell.getStdIn().add("-R");
    Ls.execute(shell);
    String expected  =
          "/rootFile\n"
        + "/dir1\n"
        + "/dir1/dir1File1\n"
        + "/dir1/dir1File2\n"
        + "/dir1/dir11\n"
        + "/dir1/dir12\n"
        + "/dir1/dir12/dir12File\n"
        + "/dir2\n";
    assertEquals(shell.getStdOut().toString(), expected);
  }
  
  /**
   * Tests ls with -R with a provided path.
   */
  @Test
  public void executeRecursiveWithPathTest(){  
    shell.getStdIn().add("-R");
    shell.getStdIn().add("dir1");
    Ls.execute(shell);
    String expected  =
        "/dir1:\n"
      + "/dir1/dir1File1\n"
      + "/dir1/dir1File2\n"
      + "/dir1/dir11\n"
      + "/dir1/dir12\n"
      + "/dir1/dir12/dir12File\n";
    assertEquals(shell.getStdOut().toString(), expected);
  }
  
  /**
   * Tests ls without -R with a provided path.
   */
  @Test
  public void executeWithPathTest(){
    shell.getStdIn().add("dir1");
    Ls.execute(shell);
    String expected  =
        "dir1:\n"
      + "dir1File1\n"
      + "dir1File2\n"
      + "dir11\n"
      + "dir12\n";
    assertEquals(shell.getStdOut().toString(), expected);
  }
  
  /**
   * Tests ls without -R nor a provided path.
   */
  @Test
  public void executeNoPathTest(){
    Ls.execute(shell);
    String expected  =
        "rootFile\n"
      + "dir1\n"
      + "dir2\n";
    assertEquals(shell.getStdOut().toString(), expected);
  }
  
  /**
   * Tests ls without -R with a provided full path.
   */
  @Test
  public void executeFileFullPathTest(){
    shell.getStdIn().add("/dir1/dir12/dir12File");
    Ls.execute(shell);
    String expected  =
        "dir12File\n\n";
    assertEquals(shell.getStdOut().toString(), expected);
  }
  
  /**
   * Tests ls without -R with a provided relative path.
   */
  @Test
  public void executeFileRelativePathTest(){
    shell.getStdIn().add("dir1/dir1File2");
    Ls.execute(shell);
    String expected  =
        "dir1File2\n\n";
    assertEquals(shell.getStdOut().toString(), expected);
  }
  
  /**
   * Tests ls  with -R on a non-existant directory/file.
   */
  @Test
  public void executeRecursiveErrorTest(){
    shell.getStdIn().add("-R");
    shell.getStdIn().add("bla");
    Ls.execute(shell);
    String expected  =
      "Directory or File with the given path does not exist.\n";
    assertEquals(shell.getStdErr().toString(), expected);
  }
  
  /**
   * Tests ls  without -R on a non-existant directory/file.
   */
  @Test
  public void executeErrorTest(){
    shell.getStdIn().add("bla");
    Ls.execute(shell);
    String expected  =
      "Directory or File with the given path does not exist.\n";
    assertEquals(shell.getStdErr().toString(), expected);
  }
}
