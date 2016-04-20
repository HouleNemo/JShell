package test;

import static org.junit.Assert.*;

import java.lang.reflect.Field;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import commands.Mv;
import driver.Directory;
import driver.File;
import driver.JShell;
import driver.WorkingDir;

public class MvTest {

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
   * Tests the moving and overwriting of files in the same directory.
   */
  @Test
  public void testMvFile() {
    shell.getStdIn().add("/src/fileDir/file1");
    shell.getStdIn().add("/src/fileDir/file4");
    Mv.execute(shell);
    shell.getWorkingDir().changeToChild("src");
    shell.getWorkingDir().changeToChild("fileDir");
    Assert.assertNotNull(
        shell.getWorkingDir().getCurrentDirectory().getFile("file4"));
    shell.getStdIn().add("../fileDir/file4");
    shell.getStdIn().add("/src/fileDir/file2");
    Mv.execute(shell);
    Assert.assertEquals(
        shell.getWorkingDir().getCurrentDirectory().getFile("file2").
        getContent(),"file1 content");
    Assert.assertNull(
        shell.getWorkingDir().getCurrentDirectory().getFile("file4"));
    Assert.assertNull(
        shell.getWorkingDir().getCurrentDirectory().getFile("file1"));
  }

  /**
   * Tests moving an empty directory around
   */
  @Test
  public void testMvEmptyDir() {
    shell.getStdIn().add("src/emptyDir");
    shell.getStdIn().add("/src/dirDir");
    Mv.execute(shell);
    shell.getWorkingDir().changeToChild("src");
    shell.getWorkingDir().changeToChild("dirDir");
    Assert.assertNotNull(
        shell.getWorkingDir().getCurrentDirectory().getChild("emptyDir"));
    shell.getStdIn().add("emptyDir");
    shell.getStdIn().add("../emptyDirMoved");
    Mv.execute(shell);
    shell.getWorkingDir().changeToParent();
    Assert.assertNotNull(
        shell.getWorkingDir().getCurrentDirectory().getChild("emptyDirMoved"));
    Assert.assertNull(
        shell.getWorkingDir().getCurrentDirectory().getChild("emptyDir"));
  }

  /**
   * Tests moving files to other directories
   */
  @Test
  public void testMvFileDir() {
    shell.getStdIn().add("/src/testFile");
    shell.getStdIn().add("/src/../src/emptyDir");
    Mv.execute(shell);
    shell.getWorkingDir().changeToChild("src");
    shell.getWorkingDir().changeToChild("emptyDir");
    Assert.assertNotNull(
        shell.getWorkingDir().getCurrentDirectory().getFile("testFile"));
    shell.getWorkingDir().changeToParent();
    Assert.assertNull(
        shell.getWorkingDir().getCurrentDirectory().getFile("testFile"));
  }

  /**
   * Tests directory moving and renaming
   */
  @Test
  public void testMvDirDir() {
    shell.getStdIn().add("src/dirDir/dir1");
    shell.getStdIn().add("src/dirDir/dir4");
    Mv.execute(shell);
    shell.getWorkingDir().changeToChild("src");
    shell.getWorkingDir().changeToChild("dirDir");
    Assert.assertNotNull(
        shell.getWorkingDir().getCurrentDirectory().getChild("dir4"));
    Assert.assertNull(
        shell.getWorkingDir().getCurrentDirectory().getChild("dir1"));
    shell.getStdIn().add("dir4");
    shell.getStdIn().add("dir2");
    Mv.execute(shell);
    Assert.assertNull(
        shell.getWorkingDir().getCurrentDirectory().getChild("dir4"));
    shell.getWorkingDir().changeToChild("dir2");
    Assert.assertNotNull(
        shell.getWorkingDir().getCurrentDirectory().getChild("dir4"));
    shell.getStdIn().add("/src/dirDir/dir2/dir4");
    shell.getStdIn().add("/src");
    Mv.execute(shell);
    shell.getWorkingDir().changeToParent();
    shell.getWorkingDir().changeToParent();
    Assert.assertNotNull(
        shell.getWorkingDir().getCurrentDirectory().getChild("dir4"));
    shell.getStdIn().add("/src/dirDir/dir2/");
    shell.getStdIn().add("/");
    Mv.execute(shell);
    shell.getWorkingDir().changeToParent();
    Assert.assertNotNull(
        shell.getWorkingDir().getCurrentDirectory().getChild("dir2"));
  }

  /**
   * Tests moving files into directories and renaming them and moving
   * the resulting directory around
   */
  @Test
  public void testMvMixDir() {
    shell.getStdIn().add("src/mixDir/file");
    shell.getStdIn().add("src/mixDir/dir/fileMoved");
    Mv.execute(shell);
    shell.getWorkingDir().changeToChild("src");
    shell.getWorkingDir().changeToChild("mixDir");
    Assert.assertNull(
        shell.getWorkingDir().getCurrentDirectory().getChild("file"));
    shell.getWorkingDir().changeToChild("dir");
    Assert.assertNotNull(
        shell.getWorkingDir().getCurrentDirectory().getFile("fileMoved"));
    shell.getStdIn().add("../../mixDir");
    shell.getStdIn().add("/src/mixDirMoved");
    Mv.execute(shell);
    Assert.assertNotNull(
        shell.getWorkingDir().getCurrentDirectory().getFile("fileMoved"));
    shell.getWorkingDir().changeToParent();
    Assert.assertNotNull(
        shell.getWorkingDir().getCurrentDirectory().getChild("dir"));
    shell.getWorkingDir().changeToParent();
    Assert.assertNull(
        shell.getWorkingDir().getCurrentDirectory().getChild("mixDir"));
    Assert.assertNotNull(
        shell.getWorkingDir().getCurrentDirectory().getChild("mixDirMoved"));
  }
  
  /**
   * Tests to make sure illegal cases fail
   */
  @Test
  public void testIllegalMoves() {
    shell.getStdIn().add("src/emptyDir");
    shell.getStdIn().add("src/emptyDir/emptyDirMoved");
    Mv.execute(shell);
    Assert.assertFalse(shell.getStdErr().isEmpty());
    shell.getStdErr().clear();
    shell.getStdIn().add("src/dirDir/dir1");
    shell.getStdIn().add("src/fileDir/file1");
    Mv.execute(shell);
    Assert.assertFalse(shell.getStdErr().isEmpty());
    shell.getStdErr().clear();
    shell.getStdIn().add("src/dirDir/dir1");
    shell.getStdIn().add("src/fileDir/file1/");
    Mv.execute(shell);
    Assert.assertFalse(shell.getStdErr().isEmpty());
    shell.getStdErr().clear();
    shell.getWorkingDir().changeToChild("src");
    shell.getStdIn().add("./");
    shell.getStdIn().add("/src/dirDir/dir1");
    Mv.execute(shell);
    Assert.assertFalse(shell.getStdErr().isEmpty());
  }

}
