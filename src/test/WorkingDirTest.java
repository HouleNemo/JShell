package test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.NoSuchElementException;
import driver.*;

public class WorkingDirTest {
  
  private Directory root;
  private Directory dir1;
  private Directory dir1a;
  private Directory dir2;
  private WorkingDir wd;
  private WorkingDir wd2;
  private WorkingDir wd3;
  
  @Before
  public void setUp(){
    root = new Directory("/");
    dir1 = new Directory("dir1", root);
    dir1a = new Directory("dir1a", dir1);
    dir2 = new Directory("dir2", root);
    wd = new WorkingDir(dir1a);
    wd2 = new WorkingDir(root);
    wd3 = new WorkingDir(dir1);
  }
  
  /**
   * Test if getter of path works.
   */
  @Test
  public void getPathTest() {
    assertEquals(wd.getPath(), "/dir1/dir1a");
    assertEquals(wd2.getPath(), "/");
  }
  
  /**
   * Test if getter of current directory works.
   */
  @Test
  public void getCurrentDirectoryTest() {
    assertEquals(wd.getCurrentDirectory(), dir1a);
    assertEquals(wd2.getCurrentDirectory(), root);
    assertEquals(wd3.getCurrentDirectory(), dir1);
  }
  
  /**
   * Test if the working directory can move to its parent directory.
   */
  @Test
  public void changeToParentTest() {
    wd.changeToParent();
    wd2.changeToParent();
    wd3.changeToParent();
    assertEquals(wd.getCurrentDirectory(), dir1);
    assertEquals(wd2.getCurrentDirectory(), root);
    assertEquals(wd3.getCurrentDirectory(), root);
  }
  
  /**
   * Test if the working directory can move to its child directory.
   */
  @Test
  public void changeToChildTest(){
    wd2.changeToChild("dir2");
    wd3.changeToChild("dir1a");
    assertEquals(wd2.getCurrentDirectory(), dir2);
    assertEquals(wd3.getCurrentDirectory(), dir1a);
  }
  
  /**
   * Test if the working directory throws an exception when the target address
   * does not exist.
   */
  @Test(expected = NoSuchElementException.class)
  public void changeToNonExistantChildTest(){
    wd.changeToChild("This name does not exist.");
  }

}
