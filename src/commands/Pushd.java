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
import java.util.*;

/**
 * This is the class for the Pushd command.
 * 
 * @author Ming Yi Chan, Chengyi Zhang, Raghavan Chandrabalan, and Hao Chen
 *
 */
public class Pushd {


  /**
   * Pushes a Directory onto a stack and changes the given Working Directory to
   * a specified path
   * 
   * @param shell - the shell that contains the input, output, working
   *        directory, and working directory stack
   */
  public static void execute(JShell shell) {
    // Stores the path that the shell's working directory will change to
    String newWDPath = shell.getStdIn().poll();

    // Only one argument is expected
    if (!shell.getStdIn().isEmpty()) {
      shell.getStdErr().add("Too many parameters.");
      return;
    }

    // Check if the new directory path is provided
    if (newWDPath == null) {
      shell.getStdErr().add("Missing argument.");
    } else {
      // If it is, push it onto the stack
      try {
        pushToStack(shell.getWorkingDir(), newWDPath,
            shell.getDirectoryStack());
      } catch (NoSuchElementException e) {
        shell.getStdErr().add("Invalid path.");
      }
    }
  }


  /**
   * Pushes the working directory to the stack and changes the current working
   * directory to the given path without error handling.
   * 
   * @param workingDir - The working directory to push onto the stack
   * @param path - The path to change the working directory to
   * @param directoryStack - The directory stack to push WorkingDir to.
   */
  protected static void pushToStack(WorkingDir workingDir, String path,
      Stack<WorkingDir> directoryStack) {
    try {
      // Push the current directory onto the stack
      directoryStack.push(new WorkingDir(workingDir.getCurrentDirectory()));
      // Change into the new directory path
      Cd.changeDirectory(workingDir, path);
    } catch (NoSuchElementException e) {
      Popd.popFromStack(workingDir, directoryStack);
      throw new NoSuchElementException();
    }
  }


  /**
   * Returns a description of the command
   * 
   * @return The description of the command as a string.
   */
  public static String describeCmd() {
    return "pushd DIR\n"
        + "This command will take the current working directory and push it\n"
        + "on to a stack accessible with \"popd\".\n"
        + "The shell's working directory will then be changed according to\n"
        + "the given absolute or relative path by DIR.";
  }

}
