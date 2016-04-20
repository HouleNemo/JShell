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
package commands;

import driver.*;
import driver.File;

import java.io.*;
import java.net.*;
import java.util.*;


/**
 * This is the class for the Get command.
 * 
 * @author Ming Yi Chan, Chengyi Zhang, Raghavan Chandrabalan, and Hao Chen
 *
 */
public class Get {

  /**
   * Execute the command "get". Use it to retrieve the content of a website and
   * store it to a file with certain name.
   * 
   * @param shell - The main shell in which this command is called.
   */
  public static void execute(JShell shell) {
    String address = shell.getStdIn().poll();

    try {
      URL url = new URL(address);
      String fileName = formatPath(address);
      URLConnection urlc = url.openConnection();

      BufferedReader in =
          new BufferedReader(new InputStreamReader(urlc.getInputStream()));
      String content = "";
      String line;
      while ((line = in.readLine()) != null) {
        content += line + "\n";
      }
      content = content.trim();
      in.close();
      Map<String, File> f =
          shell.getWorkingDir().getCurrentDirectory().getAllFiles();
      if (f.containsKey(fileName)) {
        f.get(fileName).erase();
        f.get(fileName).append(content);
      } else {
        File file =
            new File(fileName, shell.getWorkingDir().getCurrentDirectory());
        file.append(content);
      }
    } catch (MalformedURLException e) {
      shell.getStdErr().add("The URL address is invalid.");
      return;
    } catch (IOException e) {
      shell.getStdErr()
          .add("There is something wrong with the content of the web.");
    }

  }

  /**
   * Return the file name of the retrieved content.
   * 
   * @param prePath
   * @return Returns a file name for the content retrieved
   */
  private static String formatPath(String prePath) {
    String name = "";
    if (prePath.endsWith("/")) {
      prePath = prePath.substring(0, prePath.length() - 1);
    }
    name = prePath.substring(prePath.lastIndexOf("/") + 1);
    return name;
  }

  /**
   * Returns the description of the command "get".
   * 
   * @return Returns a string describing the command "get".
   */
  public static String describeCmd() {
    return "get URL\n"
        + "Retrieve the file at given URL and add it to the current working \n"
        + "directory. The name of the file depends on the end of the URL. \n"
        + "If a file with that name already exists, cover the original file.";
  }
}
