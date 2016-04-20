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
import java.util.*;

import org.junit.*;

import driver.*;

public class MkdirTest {
  private JShell shell;
  private Directory exp;

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
   * Test the basic case of mkdir.
   */
  @Test
  public void testSingle() {
    // Construct a mock file system and create a folder.
    String[] input = {"folder1"};
    shell.getStdIn().addAll(input);
    commands.Mkdir.execute(shell);
    // Manually create a directory and a subdirectory in it.
    Directory child = new Directory("folder1", exp);
    // List the names of subfolders in each folder, check if the lists are
    // equivalent.
    String[] actual =
        shell.getWorkingDir().getCurrentDirectory().getAllChildDirNames();
    String[] expect = exp.getAllChildDirNames();
    Assert.assertTrue("oh no single", Arrays.equals(actual, expect));
  }

  /**
   * Test if Mkdir returns an error when the path includes invalid characters.
   */
  @Test
  public void testSpecialCharacter() {
    String invalidChs = "~!@#$%^&*()_+`-=[]\\{}|;':\",<>?";
    for (String invalidCh : invalidChs.split("")) {
      shell.getStdIn().add(invalidCh);
      commands.Mkdir.execute(shell);
      String[] expect =
          shell.getWorkingDir().getCurrentDirectory().getAllChildDirNames();
      Assert.assertFalse(shell.getStdErr().peek() == null);
      Assert.assertTrue(expect.length == 1);
    }
  }

  /**
   * Test if Mkdir returns an error when duplication happens.
   */
  @Test
  public void testDuplication() {
    String[] input = {"f1"};
    File temp = new File(
        "f1", shell.getWorkingDir().getCurrentDirectory());
    temp.append("\"hi\"");
    shell.getWorkingDir().getCurrentDirectory().addFile(temp);
    shell.getStdIn().addAll(input);
    commands.Mkdir.execute(shell);
    String[] expect =
        shell.getWorkingDir().getCurrentDirectory().getAllChildDirNames();
    Assert.assertFalse(shell.getStdErr().peek() == null);
    Assert.assertTrue(expect.length == 1);
  }

  /**
   * Test if mkdir can create several folders at the same time.
   */
  @Test
  public void testSeveral() {
    String[] input = {"folder1", "folder2", "folder3"};
    shell.getStdIn().addAll(input);
    commands.Mkdir.execute(shell);
    Directory child1 = new Directory("folder1", exp);
    Directory child2 = new Directory("folder2", exp);
    Directory child3 = new Directory("folder3", exp);
    String[] actual =
        shell.getWorkingDir().getCurrentDirectory().getAllChildDirNames();
    String[] expect = exp.getAllChildDirNames();
    Assert.assertTrue("oh no Several", Arrays.equals(actual, expect));
  }

  /**
   * Test if mkdir can create folders inside a folder.
   */
  @Test
  public void testInside() {
    String[] input1 = {"folder1"};
    shell.getStdIn().addAll(input1);
    commands.Mkdir.execute(shell);
    // Use the path relative to the current folder, so that the test of the
    // operation for relative path is combined in this one.
    String[] input2 = {"folder1/folder2"};
    shell.getStdIn().addAll(input2);
    commands.Mkdir.execute(shell);
    Directory child1 = new Directory("folder1", exp);
    Directory child2 = new Directory("folder2", child1);
    String[] actual = shell.getWorkingDir().getCurrentDirectory()
        .getChild("folder1").getAllChildDirNames();
    String[] expect = exp.getChild("folder1").getAllChildDirNames();
    Assert.assertTrue("oh no Inside", Arrays.equals(actual, expect));
  }

  /**
   * Test if mkdir can create several folders inside several folders,
   * respectively. This test also combines the cases of relative paths.
   */
  @Test
  public void testSeveralInside() {
    String[] input1 = {"folder1", "folder2"};
    shell.getStdIn().addAll(input1);
    commands.Mkdir.execute(shell);
    String[] input2 = {"folder1/folder3", "folder2/folder4"};
    shell.getStdIn().addAll(input2);
    commands.Mkdir.execute(shell);
    Directory child1 = new Directory("folder1", exp);
    Directory child2 = new Directory("folder2", exp);
    Directory child3 = new Directory("folder3", child1);
    Directory child4 = new Directory("folder4", child2);
    Directory current = shell.getWorkingDir().getCurrentDirectory();
    String[] actual = current.getAllChildDirNames();
    String[] expect = exp.getAllChildDirNames();
    String[] actual1 = shell.getWorkingDir().getCurrentDirectory()
        .getChild("folder1").getAllChildDirNames();
    String[] actual2 = shell.getWorkingDir().getCurrentDirectory()
        .getChild("folder2").getAllChildDirNames();
    String[] expect1 = exp.getChild("folder1").getAllChildDirNames();
    String[] expect2 = exp.getChild("folder2").getAllChildDirNames();
    Assert.assertTrue("oh no SeveralInside", Arrays.equals(actual, expect));
    Assert.assertTrue("oh no SeveralInside", Arrays.equals(actual1, expect1));
    Assert.assertTrue("oh no SeveralInside", Arrays.equals(actual2, expect2));
  }

  /**
   * Test if mkdir can create folders and subfolders with relative paths in just
   * one turn.
   */
  @Test
  public void testCombineInOne() {
    String[] input1 =
        {"folder1", "folder2", "folder1/folder3", "folder2/folder4"};
    shell.getStdIn().addAll(input1);
    commands.Mkdir.execute(shell);
    Directory child1 = new Directory("folder1", exp);
    Directory child2 = new Directory("folder2", exp);
    Directory child3 = new Directory("folder3", child1);
    Directory child4 = new Directory("folder4", child2);
    Directory current = shell.getWorkingDir().getCurrentDirectory();
    String[] actual = current.getAllChildDirNames();
    String[] expect = exp.getAllChildDirNames();
    String[] actual1 = shell.getWorkingDir().getCurrentDirectory()
        .getChild("folder1").getAllChildDirNames();
    String[] actual2 = shell.getWorkingDir().getCurrentDirectory()
        .getChild("folder2").getAllChildDirNames();
    String[] expect1 = exp.getChild("folder1").getAllChildDirNames();
    String[] expect2 = exp.getChild("folder2").getAllChildDirNames();
    Assert.assertTrue("oh no CombineInOne", Arrays.equals(actual, expect));
    Assert.assertTrue("oh no CombineInOne", Arrays.equals(actual1, expect1));
    Assert.assertTrue("oh no CombineInOne", Arrays.equals(actual2, expect2));
  }

  /**
   * Test if mkdir can operate the special cases of paths, such as "..", ".",
   * and full path.
   */
  @Test
  public void testDifferentPath() {
    String[] input1 = {"folder1"};
    shell.getStdIn().addAll(input1);
    commands.Mkdir.execute(shell);
    // Move down to the first subfolder, so that the relative path and full path
    // are different.
    shell.getStdIn().addAll(input1);
    commands.Cd.execute(shell);
    String[] input2 = {"../folder2", "./folder3", "/folder2/folder4"};
    shell.getStdIn().addAll(input2);
    commands.Mkdir.execute(shell);
    // Move back to the root folder to continue the testing steps.
    String[] input3 = {"/"};
    shell.getStdIn().addAll(input3);
    commands.Cd.execute(shell);
    Directory child1 = new Directory("folder1", exp);
    Directory child2 = new Directory("folder2", exp);
    Directory child3 = new Directory("folder3", child1);
    Directory child4 = new Directory("folder4", child2);
    Directory current = shell.getWorkingDir().getCurrentDirectory();
    String[] actual = current.getAllChildDirNames();
    String[] expect = exp.getAllChildDirNames();
    String[] actual1 = shell.getWorkingDir().getCurrentDirectory()
        .getChild("folder1").getAllChildDirNames();
    String[] actual2 = shell.getWorkingDir().getCurrentDirectory()
        .getChild("folder2").getAllChildDirNames();
    String[] expect1 = exp.getChild("folder1").getAllChildDirNames();
    String[] expect2 = exp.getChild("folder2").getAllChildDirNames();
    Assert.assertTrue("oh no DifferentPath", Arrays.equals(actual, expect));
    Assert.assertTrue("oh no DifferentPath", Arrays.equals(actual1, expect1));
    Assert.assertTrue("oh no DifferentPath", Arrays.equals(actual2, expect2));
  }
}
