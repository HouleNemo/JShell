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
import java.util.Stack;

import driver.*;

/**
 * This is the class for the Mkdir command.
 * 
 * @author Ming Yi Chan, Chengyi Zhang, Raghavan Chandrabalan, and Hao Chen
 *
 */
public class Mkdir {


  /**
   * Will make a directory in the given shell based on the contained inputs.
   * 
   * @param shell - The shell to create a directory in.
   */
  public static void execute(JShell shell) {
    // While the next item in the stream is not a command
    while (shell.getStdIn().peek() != null) {
      // Get argument
      String argument = shell.getStdIn().poll();

      // Make the directory
      String errors = mkdirFromPath(shell.getWorkingDir(), argument,
          shell.getDirectoryStack());

      // If an error occurred, write it to standard error
      if (!errors.isEmpty()) {
        shell.getStdErr().add(errors);
      }
    }
  }


  /**
   * Will make a directory based on the path given by "argument" in the file
   * system that is accessed with "workingDir".
   * 
   * @param workingDir - The WorkingDir of the file system used.
   * @param argument - The path/name of the directory to create.
   * @param directoryStack - The directory stack used to store previously
   *        accessed locations.
   * @return Will return an empty string if there are no errors. Otherwise, the
   *         string will correspond to the error.
   */
  private static String mkdirFromPath(WorkingDir workingDir, String argument,
      Stack<WorkingDir> directoryStack) {
    String path = "";
    String name = "";
    // Reject any path with invalid characters.
    String[] invalidChs = {"~","!","@","#","$","%","^","&","*","(",")","_","+"
        ,"`","-","=","[","]","\\","{","}","|",";","'",":","\"",",","<",">","?"};
    for (String invalidCh : invalidChs) {
      if (argument.contains(invalidCh)) {
        return "Invalid path name with character: " + invalidCh;
      }
    }

    // If a path is given
    if (argument.contains("/")) {
      // Separate the path from the name
      path = Cd.getPathFromString(argument);
      name = argument.substring(path.length());

      // Use pushd to go to directory
      try {
        Pushd.pushToStack(workingDir, path, directoryStack);
      } catch (NoSuchElementException e) {
        return "Invalid path in " + argument;
      }
    } else {
      name = argument;
    }

    // Write error if the argument is an invalid directory name.
    if (!workingDir.getCurrentDirectory().isValidName(name)) {
      if (!path.isEmpty()) {
        Popd.popFromStack(workingDir, directoryStack);
      }
      return argument
          + " is already being used in the current directory. Please use a\n"
          + "different name.";
    }

    // Create new directory
    @SuppressWarnings("unused")
    Directory temp = new Directory(name, workingDir.getCurrentDirectory());

    // If a path was given, return to original directory
    if (!path.isEmpty()) {
      Popd.popFromStack(workingDir, directoryStack);
    }
    return "";
  }


  /**
   * Returns a description of the command
   * 
   * @return The description of the command as a string.
   */
  public static String describeCmd() {
    return "mkdir DIR1 ...\n"
        + "Given a list of directory paths, create the directories. The paths\n"
        + "can be absolute or relative.";
  }

}
