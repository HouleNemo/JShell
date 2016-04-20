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

import driver.JShell;
import java.util.List;

/**
 * This is the class for the redo command (called with ![NUMBER] in JShell.)
 * 
 * @author Ming Yi Chan, Chengyi Zhang, Raghavan Chandrabalan, and Hao Chen
 *
 */
public class Redo {

  /**
   * Executes the ![NUMBER] command. Finds the command stored in history at the
   * input history number and recalls it in JShell.
   * 
   * @param shell - The shell to create a directory in.
   */
  public static void execute(JShell shell) {
    try {
      String inCmd = shell.getCmd().substring(1);
      // Ensure there is no further parameters.
      if (shell.getStdIn().peek() != null) {
        throw new TooManyParametersException();
      }
      shell.getStdIn().clear();
      // Parse out the number parameter. Ensure it is a positive int.
      double num = History.checkArgs(inCmd);
      List<String> history = shell.getShellHistory().getList();
      if(num >= history.size()){
        throw new IndexOutOfBoundsException();
      }
      // Find the command recorded at the input parameter index.
      String[] redo = history.get((int)num - 1)
          .split("\\s+(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
      // Let the first recorded string be the command.
      shell.setCmd(redo[0]);
      // Load stored parameters into StdIn. 
      for (int i = 1; i < redo.length; i++) {
        shell.getStdIn().add(redo[i]);
      }
      // Execute the command.
      shell.switchBoard();
    } catch (TooManyParametersException e) {
      shell.getStdErr().add("Too many parameters.");
    } catch (IllegalArgumentException f) {
      shell.getStdErr().add("Parameter must be a positive integer.");
    } catch (IndexOutOfBoundsException g) {
      shell.getStdErr().add("Parameter exceeds bounds of history log.");
    } catch (StackOverflowError e) {
      shell.getStdErr().add("You are building an infinite loop!");
    }
  }

  /**
   * Returns a description of the command.
   * 
   * @return The description of the command as a string.
   */
  public static String describeCmd() {
    return "man ![NUMBER]\n"
        + "This command must be followed by a positive integer. This command\n"
        + "will recall any stored command in the JShell history at the input\n"
        + "history number.";
  }

}
