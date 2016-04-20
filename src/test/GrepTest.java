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
import commands.Grep;
import driver.Directory;
import driver.File;
import driver.JShell;

public class GrepTest {

  private JShell shell;
  
  /**
   * File system fixture for GrepTest.
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
        .append("This line has blah.\nThis line does not.");
    shell.getWorkingDir().changeToChild("dir1");
    shell.getWorkingDir().getCurrentDirectory().addFile(
        new File("dir1File1", shell.getWorkingDir().getCurrentDirectory()));
    shell.getWorkingDir().getCurrentDirectory().getFile("dir1File1")
        .append("blah blah blah.\nSomething.\nMore blah.");
    shell.getWorkingDir().getCurrentDirectory().addFile(
        new File("dir1File2", shell.getWorkingDir().getCurrentDirectory()));
    shell.getWorkingDir().getCurrentDirectory().getFile("dir1File2")
        .append("Stuff.");
    shell.getWorkingDir().getCurrentDirectory().addDir(
        new Directory("dir11", shell.getWorkingDir().getCurrentDirectory()));
    shell.getWorkingDir().getCurrentDirectory().addDir(
        new Directory("dir12", shell.getWorkingDir().getCurrentDirectory()));
    shell.getWorkingDir().changeToChild("dir12");
    shell.getWorkingDir().getCurrentDirectory().addFile(
        new File("dir12File", shell.getWorkingDir().getCurrentDirectory()));
    shell.getWorkingDir().getCurrentDirectory().getFile("dir12File")
        .append("More blahs!\nSo many blahs\nNone here though...");
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
   * Tests grep without -R on some file.
   */
  @Test
  public void executeGrepOneFileTest1() {
    shell.getStdIn().add("\"blah\"");
    shell.getStdIn().add("dir1/dir1File1");
    Grep.execute(shell);
    String expected  =
        "blah blah blah.\n"
      + "More blah.\n";
    assertEquals(expected, shell.getStdOut().toString());
  }
  
  /**
   * Tests grep without -R on some file.
   */
  @Test
  public void executeGrepOneFileTest2(){
    shell.getStdIn().add("\"n\"");
    shell.getStdIn().add("/dir1/dir12/dir12File");
    Grep.execute(shell);
    String expected  =
        "So many blahs\n"
      + "None here though...\n";
    assertEquals(expected, shell.getStdOut().toString());
  }
  
  /**
   * Tests grep with -R on some file.
   */
  @Test
  public void executeGrepRecursiveTest1(){
    shell.getStdIn().add("-R");
    shell.getStdIn().add("\"blah\"");
    shell.getStdIn().add("/");
    Grep.execute(shell);
    String expected =
        "/dir1/dir12/dir12File:\n"
      + "More blahs!\n"
      + "So many blahs\n"
      + "\n"
      + "/dir1/dir1File1:\n"
      + "blah blah blah.\n"
      + "More blah.\n"
      + "\n"
      + "/rootFile:\n"
      + "This line has blah.\n\n";
    assertEquals(expected, shell.getStdOut().toString());
  }
  
  /**
   * Tests grep with -R on some file.
   */
  @Test
  public void executeGrepRecursiveTest2(){
    shell.getStdIn().add("-R");
    shell.getStdIn().add("\"S\"");
    shell.getStdIn().add("dir1");
    Grep.execute(shell);
    String expected =
        "/dir1/dir12/dir12File:\n"
      + "So many blahs\n"
      + "\n"
      + "/dir1/dir1File1:\n"
      + "Something.\n"
      + "\n"
      + "/dir1/dir1File2:\n"
      + "Stuff.\n\n";
    assertEquals(expected, shell.getStdOut().toString());
  }
  
  /**
   * Tests whether Grep prints the MissingQuotesException error message into
   * StdErr.
   */
  @Test
  public void executeGrepQuoteErrorTest(){
    shell.getStdIn().add("-R");
    shell.getStdIn().add("Unquoted");
    shell.getStdIn().add("/");
    Grep.execute(shell);
    String expected = "The input string needs to be surrounded by double"
          + " quotation marks.\n";
    assertEquals(expected, shell.getStdErr().toString());
    assertEquals("", shell.getStdOut().toString());
  }
  
  /**
   * Tests whether Grep prints the NullPointerException error message into
   * StdErr.
   */
  @Test
  public void executeGrepFileDNEErrorTest(){
    shell.getStdIn().add("\"blah\"");
    shell.getStdIn().add("/dir1/InexistentFile");
    Grep.execute(shell);
    String expected = "File with the input path does not exist.\n";
    assertEquals(expected, shell.getStdErr().toString());
    assertEquals("", shell.getStdOut().toString());
  }
  
  /**
   * Tests whether Grep prints the NoSuchElementException error message into
   * StdErr.
   */
  @Test
  public void executeGrepDirectoryDNEErrorTest(){
    shell.getStdIn().add("-R");
    shell.getStdIn().add("\"blah\"");
    shell.getStdIn().add("/SomethingThatDoesNotExist");
    Grep.execute(shell);
    String expected = "Directory with the input path does not exist.\n";
    assertEquals(expected, shell.getStdErr().toString());
    assertEquals("", shell.getStdOut().toString());
  }
  
  /**
   * Tests whether Grep prints the TooManyParametersException error message
   * into StdErr.
   */
  @Test
  public void executeGrepTooManyParametersErrorTest(){
    shell.getStdIn().add("-R");
    shell.getStdIn().add("\"blah\"");
    shell.getStdIn().add("/dir1");
    shell.getStdIn().add("EnterBullshitHere");
    Grep.execute(shell);
    String expected = "Too many parameters.\n";
    assertEquals(expected, shell.getStdErr().toString());
    assertEquals("", shell.getStdOut().toString());
  }
}
