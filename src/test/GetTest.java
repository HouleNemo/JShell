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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.URL;
import java.net.URLConnection;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import commands.Cat;
import commands.Echo;
import commands.Get;
import driver.*;

/**
 * This is the test class for the Get command.
 * 
 * @author Ming Yi Chan, Chengyi Zhang, Raghavan Chandrabalan, and Hao Chen
 *
 */
public class GetTest {
  private JShell shell;
  String[] contents = {"", ""};

  // Build a file system JShell and get the contents from some certain websites
  @Before
  public void setUp() throws Exception {
    shell = JShell.createJShell();
    String[] urls = {"http://www.cs.cmu.edu/~spok/grimmtmp/073.txt",
        "http://www.math.toronto.edu/courses/mat237y1/20159/index.html"};
    for (int i = 0; i < urls.length; i++) {
      URL url = new URL(urls[i]);
      URLConnection urlc = url.openConnection();
      BufferedReader in =
          new BufferedReader(new InputStreamReader(urlc.getInputStream()));
      String line;
      while ((line = in.readLine()) != null) {
        contents[i] += line + "\n";
      }
      in.close();
    }
  }

  // Reset the JShellRef to null such that setUp can build a new shell
  @After
  public void tearDown() throws Exception {
    Field field = (shell.getClass()).getDeclaredField("JShellRef");
    field.setAccessible(true);
    field.set(null, null);
  }

  // Test if Get retrieves content from website ending with txt
  @Test
  public void testTxt() {
    String[] input = {"http://www.cs.cmu.edu/~spok/grimmtmp/073.txt"};
    shell.getStdIn().addAll(input);
    Get.execute(shell);
    String[] input2 = {"073.txt"};
    shell.getStdIn().addAll(input2);
    Cat.execute(shell);
    String expected = contents[0];
    String actual = shell.getStdOut().toString();
    Assert.assertTrue(actual.equals(expected));
  }

  // Test if Get retrieves content from website ending with html
  @Test
  public void testHtml() {
    String[] input =
        {"http://www.math.toronto.edu/courses/mat237y1/20159/index.html"};
    shell.getStdIn().addAll(input);
    Get.execute(shell);
    String[] input2 = {"index.html"};
    shell.getStdIn().addAll(input2);
    Cat.execute(shell);
    String expected = contents[1];
    String actual = shell.getStdOut().toString();
    Assert.assertTrue(actual.equals(expected));
  }

  // Test if Get covers the original file if there exists a file with the same
  // name
  @Test
  public void testCover() {
    File temp = new File(
        "073.txt", shell.getWorkingDir().getCurrentDirectory());
    temp.append("\"hello\"");
    shell.getWorkingDir().getCurrentDirectory().addFile(temp);
    String[] input = {"http://www.cs.cmu.edu/~spok/grimmtmp/073.txt"};
    shell.getStdIn().addAll(input);
    Get.execute(shell);
    String[] input2 = {"073.txt"};
    shell.getStdIn().addAll(input2);
    Cat.execute(shell);
    String expected = contents[0];
    String actual = shell.getStdOut().toString();
    Assert.assertTrue(!actual.equals("hello\n"));
    Assert.assertTrue(actual.equals(expected));
  }
}
