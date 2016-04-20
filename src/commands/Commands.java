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

import java.util.*;

/**
 * This class holds a list of all supported commands on JShell and can check
 * whether an input command is supported.
 * 
 * @author Ming Yi Chan, Chengyi Zhang, Raghavan Chandrabalan, and Hao Chen
 *
 */
public class Commands {

  private static String cmds[] = {"mkdir", "cd", "ls", "pwd", "pushd", "popd",
      "history", "cat", "echo", "man", "grep", "get", "redo", "cp", "mv"};

  /**
   * Returns a list of commands supported by JShell.
   * 
   * @return Returns an array of strings whose elements is the supported
   *         commands of JShell.
   */
  public static String[] getCmds() {
    return cmds;
  }


  // Checks if the input string is a supported command.
  /**
   * Checks to see if the given string is a supported command of JShell.
   * 
   * @param cmd - The input string to be checked.
   * @return Returns true if the given string is a supported command of JShell.
   */
  public static boolean isValidCmd(String cmd) {
    try{
      if(cmd != null && cmd.charAt(0) == '!')
        return true;
    }
    catch(IndexOutOfBoundsException e){}
    return (Arrays.asList(cmds).contains(cmd));
  }
}
