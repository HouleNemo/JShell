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
 * This is the class for the Echo command.
 * 
 * @author Ming Yi Chan, Chengyi Zhang, Raghavan Chandrabalan, and Hao Chen
 *
 */
public class Echo {
  /**
   * Execute the command "echo". Print the specified content or use it modify
   * the located file in different ways following the marker. Add any possible
   * error into StdErr in JShell.
   * 
   * @param shell - The main shell in which this command is called.
   */
  public static void execute(JShell shell) {
    // Take the first param as the content to deal with.
    String content = shell.getStdIn().poll();
    // Check for param
    if (content == null) {
      shell.getStdErr().add("Missing STRING. Enter \"man echo\" to view the "
          + "manual for the echo command.");
      return;
    }
    // Check if the content is surrounded by double quotation marks.
    if (!content.startsWith("\"") || !content.endsWith("\"")) {
      shell.getStdErr()
          .add("The input string needs to be surrounded by double quotation"
              + " marks.");
      return;
    } else if (shell.getStdIn().peek() != null) {
      shell.getStdErr()
          .add("The marker before address should be \">\" or \">>\".");
    }
    // Then strip the quotation marks.
    content = content.substring(1, content.length() - 1);
    shell.getStdOut().add(content);
  }

  /**
   * Returns the description of the command "echo".
   * 
   * @return Returns a string describing the command "echo".
   */
  public static String describeCmd() {
    return "echo STRING\n"
        + "If the command is proceeded by a string S, then by >, and finally\n"
        + "a string F; the contents of the file with the name F will be\n"
        + "overwritten with the string S.\n"
        + "If the command is proceeded by a string S, then by >>, and finally\n"
        + "a string F; string S will be appended to the contents of the file\n"
        + "with the name F. \n"
        + "If the string F is not the name of any file in the current\n"
        + "directory, a new file with the name F will be created and the\n"
        + "command will continue its execution.\n"
        + "If no string F is given or if only the parameter S is given, the\n"
        + "string S is printed to the shell.\n";
  }
}
