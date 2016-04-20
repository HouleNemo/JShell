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

import java.util.NoSuchElementException;
import driver.*;

/**
 * This is the class for the Cd command.
 * 
 * @author Ming Yi Chan, Chengyi Zhang, Raghavan Chandrabalan, and Hao Chen
 *
 */
public class Cd {


  /**
   * Executes the Cd command, which changes the current working directory to the
   * directory of the input path in StdIn.
   * 
   * @param shell - The JShell object on which this command operates.
   */
  public static void execute(JShell shell) {
    // One argument expected
    if (shell.getStdIn().isEmpty()) {
      shell.getStdErr().add("Parameter missing.");
      return;
    }

    // Get argument
    String path = shell.getStdIn().poll();

    // Only one argument is expected
    if (!shell.getStdIn().isEmpty()) {
      shell.getStdErr().add("Too many parameters.");
      return;
    }

    // Get current directory path
    String workingDirBackupPath = shell.getWorkingDir().getPath();
    // Change directory into the provided path, if failed, return to original
    // directory
    try {
      Cd.changeDirectory(shell.getWorkingDir(), path);
    } catch (NoSuchElementException e) {
      shell.getStdErr().add("Directory with the given path does not exist.");
      Cd.changeDirectory(shell.getWorkingDir(), workingDirBackupPath);
    }
  }


  /**
   * Changes the current working directory to the directory at the input path.
   * 
   * @param wrkDir - The current working directory object.
   * @param path - The path of the destination directory.
   */
  protected static void changeDirectory(WorkingDir wrkDir, String path) {

    if (path.isEmpty()) { // Do nothing if the path is empty.
      return;
    }
    // If path provided begins with a '/', the provided path is an absolute
    // path. Change to parent iteratively until we reach the root.
    if (path.substring(0, 1).equals("/")) {
      while (!wrkDir.getPath().equals("/")) {
        wrkDir.changeToParent();
      }
    }
    // Split the argued path into an array of directory name tokens.
    String[] dirNames = path.split("/");
    // Iteratively change to the directory with the given name token
    // or the parent directory.
    for (String name : dirNames) {
      if (!name.equals("")) {
        if (name.equals("..")) {
          wrkDir.changeToParent();
        } else if (!name.equals(".")) {
          wrkDir.changeToChild(name);
        }
      }
    }
  }


  /**
   * Returns the path to the parent directory of the object located at the input
   * path.
   * 
   * @param argument - The input path
   * @return The path to the parent of the object located at the input path.
   */
  protected static String getPathFromString(String argument) {
    int lastSlash = argument.lastIndexOf("/") + 1;
    return argument.substring(0, lastSlash);
  }



  /**
   * Returns a string that describes what the "cd" command does.
   * 
   * @return A string describing what the "cd" command does.
   */
  public static String describeCmd() {
    return "cd DIR\n"
        + "This command changes the current working directory to the\n"
        + "directory with the argued path provided after the command. The\n"
        + "argued path may be a relative or an absolute path.\n"
        + "Enter '..' as the argued path to change to the parent directory\n"
        + "or add '.' to change to the current directory.";
  }

}
