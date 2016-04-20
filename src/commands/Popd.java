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
 * This is the class for the Popd command.
 * 
 * @author Ming Yi Chan, Chengyi Zhang, Raghavan Chandrabalan, and Hao Chen
 *
 */
public class Popd {


  /**
   * Pops the topmost WorkingDir from the stack and changes the current shell's
   * workingDir to it.
   * 
   * @param shell - the shell that contains the output, working directory, and
   *        working directory stack
   */
  public static void execute(JShell shell) {
    // No argument expected
    if (!shell.getStdIn().isEmpty()) {
      shell.getStdErr().add("Too many parameters.");
      return;
    }
    // Will attempt to pop a WorkingDir object from the stack
    try {
      Popd.popFromStack(shell.getWorkingDir(), shell.getDirectoryStack());
    } catch (EmptyStackException e) {
      shell.getStdErr().add("Stack is empty.");
    } catch (NoSuchElementException e) {
      shell.getStdErr().add("Path is no longer valid.");
    }
  }


  /**
   * Will pop the topmost WorkingDirectory on the stack and change the current
   * directory to it without any error handling.
   * 
   * @param workingDir - The working directory to change
   * @param directoryStack - The stack to pop a directory path from
   */
  protected static void popFromStack(WorkingDir workingDir,
      Stack<WorkingDir> directoryStack) {
    // Get path from stack and change directory to it
    Cd.changeDirectory(workingDir, directoryStack.pop().getPath());
  }


  /**
   * Returns a description of the command
   * 
   * @return The description of the command as a string.
   */
  public static String describeCmd() {
    return "popd\n"
        + "This command will attempt to pop the topmost directory on the\n"
        + "stack and attempt to change the shell's working directory to it.\n"
        + "This command takes no arguments.";
  }

}
