package test;

import static org.junit.Assert.*;

import java.lang.reflect.Field;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import commands.Cp;
import driver.Directory;
import driver.File;
import driver.JShell;
import driver.WorkingDir;

public class CpTest {

  private JShell shell;

  /**
   * Create JShell object with files and directory for each test case
   */
  @Before
  public void setUp() {
    // List of directories and files created:
    // /src
    // /src/testFile
    // /src/emptyDir
    // /src/fileDir
    // /src/fileDir/file1
    // /src/fileDir/file2
    // /src/dirDir
    // /src/dirDir/dir1
    // /src/dirDir/dir2
    // /src/mixDir
    // /src/mixDir/file
    // /src/mixDir/dir
    // /dest (is a directory)

    // Create JShell object
    shell = JShell.createJShell();

    // Get working directory
    WorkingDir workingDir = shell.getWorkingDir();
    // Get current directory
    Directory currentDir = workingDir.getCurrentDirectory();

    // Create source directory
    // PATH: /src
    currentDir.addDir(new Directory("src", currentDir));

    // Enter src directory
    workingDir.changeToChild("src");
    currentDir = workingDir.getCurrentDirectory();

    // Create testFile
    // PATH: /src/testFile
    createFile("testFile", currentDir);

    // Create emptyDir
    // PATH: /src/emptyDir
    currentDir.addDir(new Directory("emptyDir", currentDir));

    // Create fileDir
    // PATH: /src/fileDir
    currentDir.addDir(new Directory("fileDir", currentDir));

    // Enter fileDir directory
    workingDir.changeToChild("fileDir");
    currentDir = workingDir.getCurrentDirectory();
    // Create file1
    // PATH: /src/fileDir/file1
    createFile("file1", currentDir);
    // Create file2
    // PATH: /src/fileDir/file2
    createFile("file2", currentDir);
    // Exit fileDir
    workingDir.changeToParent();
    currentDir = workingDir.getCurrentDirectory();

    // Create dirDir
    // PATH: /src/dirDir
    currentDir.addDir(new Directory("dirDir", currentDir));

    // Enter dirDir directory
    workingDir.changeToChild("dirDir");
    currentDir = workingDir.getCurrentDirectory();
    // Create dir1
    // PATH: /src/dirDir/file1
    currentDir.addDir(new Directory("dir1", currentDir));
    // Create dir2
    // PATH: /src/dirDir/file2
    currentDir.addDir(new Directory("dir2", currentDir));
    // Exit dirDir
    workingDir.changeToParent();
    currentDir = workingDir.getCurrentDirectory();

    // Create mixDir
    // PATH: /src/mixDir
    currentDir.addDir(new Directory("mixDir", currentDir));

    // Enter mixDir directory
    workingDir.changeToChild("mixDir");
    currentDir = workingDir.getCurrentDirectory();
    // Create dir1
    // PATH: /src/mixDir/file1
    createFile("file", currentDir);
    // Create dir2
    // PATH: /src/mixDir/file2
    currentDir.addDir(new Directory("dir", currentDir));
    // Exit mixDir
    workingDir.changeToParent();
    currentDir = workingDir.getCurrentDirectory();

    // Exit src
    workingDir.changeToParent();
    currentDir = workingDir.getCurrentDirectory();

    // Create destination directory
    // PATH: /dest
    currentDir.addDir(new Directory("dest", currentDir));
  }
  
  @After
  public void tearDown() throws Exception {
    Field field = (shell.getClass()).getDeclaredField("JShellRef");
    field.setAccessible(true);
    field.set(null, null);
  }

  /**
   * Creates a file in the current directory
   * 
   * @param name - The name of the file
   * @param currentDir - The current directory
   */
  private void createFile(String name, Directory currentDir) {
    // Create new file
    File newFile = new File(name, currentDir);
    // Add content to file
    newFile.append(name + " content");
    // Add file to current directory
    currentDir.addFile(newFile);
  }

  /**
   * Tests simple file copying and renaming
   */
  @Test
  public void testCpFile() {
    shell.getStdIn().add("/src/testFile");
    shell.getStdIn().add("/src/testFile");
    Cp.execute(shell);
    shell.getStdIn().add("/src/testFile");
    shell.getStdIn().add("/src/testFileCopy");
    Cp.execute(shell);
    shell.getWorkingDir().changeToChild("src");
    Assert.assertNotNull(
        shell.getWorkingDir().getCurrentDirectory().getFile("testFileCopy"));
    Assert.assertNotNull(
        shell.getWorkingDir().getCurrentDirectory().getFile("testFile"));
    Assert.assertEquals(
        shell.getWorkingDir().getCurrentDirectory().getFile("testFileCopy").
        getContent(),"testFile content");
    shell.getStdIn().add("testFileCopy");
    shell.getStdIn().add("/../dest");
    Cp.execute(shell);
    shell.getWorkingDir().changeToParent();
    shell.getWorkingDir().changeToChild("dest");
    Assert.assertNotNull(
        shell.getWorkingDir().getCurrentDirectory().getFile("testFileCopy"));
    Assert.assertEquals(
        shell.getWorkingDir().getCurrentDirectory().getFile("testFileCopy").
        getContent(),"testFile content");
  }

  /**
   * Tests simple directory copying
   */
  @Test
  public void testCpEmptyDir() {
    shell.getStdIn().add("/src/emptyDir");
    shell.getStdIn().add("/dest/");
    Cp.execute(shell);
    shell.getWorkingDir().changeToChild("dest");
    Assert.assertNotNull(
        shell.getWorkingDir().getCurrentDirectory().getChild("emptyDir"));
    shell.getStdIn().add("emptyDir");
    shell.getStdIn().add("emptyDirCopy");
    Cp.execute(shell);
    Assert.assertNotNull(
        shell.getWorkingDir().getCurrentDirectory().getChild("emptyDir"));
    Assert.assertNotNull(
        shell.getWorkingDir().getCurrentDirectory().getChild("emptyDirCopy"));
  }

  /**
   * Test for the copying and renaming of directories.
   */
  @Test
  public void testCpdirDir() {
    shell.getStdIn().add("/src/dirDir");
    shell.getStdIn().add("/dest/");
    Cp.execute(shell);
    shell.getWorkingDir().changeToChild("dest");
    Assert.assertNotNull(
        shell.getWorkingDir().getCurrentDirectory().getChild("dirDir"));
    shell.getWorkingDir().changeToChild("dirDir");
    Assert.assertNotNull(
        shell.getWorkingDir().getCurrentDirectory().getChild("dir1"));
    shell.getStdIn().add("dir1");
    shell.getStdIn().add("dir2");
    Cp.execute(shell);
    Assert.assertNotNull(
        shell.getWorkingDir().getCurrentDirectory().getChild("dir1"));
    shell.getWorkingDir().changeToChild("dir2");
    Assert.assertNotNull(
        shell.getWorkingDir().getCurrentDirectory().getChild("dir1"));
    shell.getStdIn().add(".");
    shell.getStdIn().add("../dir1/dir2Copy");
    Cp.execute(shell);
    Assert.assertNotNull(
        shell.getWorkingDir().getCurrentDirectory().getChild("dir1"));
    shell.getWorkingDir().changeToParent();
    shell.getWorkingDir().changeToChild("dir1");
    Assert.assertNotNull(
        shell.getWorkingDir().getCurrentDirectory().getChild("dir2Copy"));
    shell.getWorkingDir().changeToChild("dir2Copy");
    Assert.assertNotNull(
        shell.getWorkingDir().getCurrentDirectory().getChild("dir1"));
  }

  /**
   * Large copy test
   */
  @Test
  public void testCpBig() {
    shell.getStdIn().add("/src/");
    shell.getStdIn().add("/dest/");
    Cp.execute(shell);
    shell.getWorkingDir().changeToChild("dest");
    Assert.assertNotNull(
        shell.getWorkingDir().getCurrentDirectory().getChild("src"));
    shell.getWorkingDir().changeToChild("src");
    Assert.assertNotNull(
        shell.getWorkingDir().getCurrentDirectory().getFile("testFile"));
    shell.getStdIn().add("../src");
    shell.getStdIn().add("../srcCopy");
    Cp.execute(shell);
    shell.getWorkingDir().changeToParent();
    Assert.assertNotNull(
        shell.getWorkingDir().getCurrentDirectory().getChild("src"));
    Assert.assertNotNull(
        shell.getWorkingDir().getCurrentDirectory().getChild("srcCopy"));
    shell.getStdIn().add("../src");
    shell.getStdIn().add("../../srcCopy2");
    Cp.execute(shell);
    shell.getWorkingDir().changeToParent();
    Assert.assertNotNull(
        shell.getWorkingDir().getCurrentDirectory().getChild("srcCopy2"));
    shell.getWorkingDir().changeToChild("srcCopy2");
    Assert.assertNotNull(
        shell.getWorkingDir().getCurrentDirectory().getChild("mixDir"));
    Assert.assertNotNull(
        shell.getWorkingDir().getCurrentDirectory().getFile("testFile"));
    shell.getWorkingDir().changeToChild("mixDir");
    Assert.assertNotNull(
        shell.getWorkingDir().getCurrentDirectory().getChild("dir"));
    Assert.assertEquals(
        shell.getWorkingDir().getCurrentDirectory().getFile("file").
        getContent(),"file content");
    
  }

  /**
   * Tests to make sure bad parameters cause errors
   */
  @Test
  public void testIllegalCopy() {
    shell.getStdIn().add("/src/emptyDir");
    shell.getStdIn().add("/src/emptyDir");
    Cp.execute(shell);
    Assert.assertFalse(shell.getStdErr().isEmpty());
    shell.getStdErr().clear();
    shell.getStdIn().add("/src/emptyDir");
    shell.getStdIn().add("/src/");
    Cp.execute(shell);
    Assert.assertFalse(shell.getStdErr().isEmpty());
    shell.getStdErr().clear();
    shell.getStdIn().add("/src/dirDir");
    shell.getStdIn().add("/src/dirDir/dir1");
    Cp.execute(shell);
    Assert.assertFalse(shell.getStdErr().isEmpty());
    shell.getStdErr().clear();
    shell.getStdIn().add("/src/dirDir");
    shell.getStdIn().add("/src/dirDir/dirDirCopy");
    Cp.execute(shell);
    Assert.assertFalse(shell.getStdErr().isEmpty());
    shell.getStdErr().clear();
    shell.getStdIn().add("/src/emptyDir");
    shell.getStdIn().add("/src/testFile");
    Cp.execute(shell);
    Assert.assertFalse(shell.getStdErr().isEmpty());
  }

}
