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

/**
 * This is the class for the Pwd command.
 * 
 * @author Ming Yi Chan, Chengyi Zhang, Raghavan Chandrabalan, and Hao Chen
 *
 */
public class Pwd {


  /**
   * Executes the "pwd" command. Prints out the path for the current working
   * directory.
   * 
   * @param shell - The JShell object on which this command operates.
   */
  public static void execute(JShell shell) {
    // No arguments expected
    if (!shell.getStdIn().isEmpty()) {
      shell.getStdErr().add("Too many parameters.");
      return;
    }
    // Get current path
    shell.getStdOut().add(Pwd.getWorkingDirPath(shell.getWorkingDir()));
  }


  /**
   * Retrieves the path of the input working directory object as a string.
   * 
   * @param dir - The current working directory
   * @return String representation of the path for the current directory
   */
  public static String getWorkingDirPath(WorkingDir dir) {
    return dir.getPath();
  }


  /**
   * Returns a string describing what the "pwd" command does.
   * 
   * @return A string that describes what the "pwd" command does.
   */
  public static String describeCmd() {
    return "pwd\n"
        + "This command prints the whole path of the current working\n"
        + "directory.";
  }

}
