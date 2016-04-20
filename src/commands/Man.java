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

import java.lang.reflect.InvocationTargetException;
import driver.*;

/**
 * This is the class for the Man command.
 * 
 * @author Ming Yi Chan, Chengyi Zhang, Raghavan Chandrabalan, and Hao Chen
 *
 */
public class Man {


  /**
   * Prints out the manual for a specific command.
   * 
   * @param shell - The current active shell.
   */
  public static void execute(JShell shell) {
    // Get argument
    String cmd = shell.getStdIn().poll();
    // Check for invalid command
    if (!shell.getStdIn().isEmpty()) {
      shell.getStdErr().add("Too many parameters.");
      return;
    }
    // Check for valid command
    if (Commands.isValidCmd(cmd)) {
      if(cmd.charAt(0) == '!'){
        shell.getStdOut().add(Redo.describeCmd());
      }
      else{
        // Capitalize first letter
        cmd = cmd.substring(0, 1).toUpperCase() + cmd.substring(1);
        // Get documentation for the command
        try {
          Class<?> cmdClass = Class.forName("commands." + cmd);
          shell.getStdOut().add((String) cmdClass.getMethod("describeCmd")
              .invoke((Object[]) null, (Object[]) null));
        } catch (IllegalAccessException e) {
          shell.getStdErr().add("Debug: IllegalAccessException");
        } catch (IllegalArgumentException e) {
          shell.getStdErr().add("Debug: IllegalArgumentException");
        } catch (InvocationTargetException e) {
          shell.getStdErr().add("Debug: InvocationTargetException");
        } catch (NoSuchMethodException e) {
          shell.getStdErr().add("Debug: NoSuchMethodException");
        } catch (SecurityException e) {
          shell.getStdErr().add("Debug: SecurityException");
        } catch (ClassNotFoundException e) {
          shell.getStdErr().add("Invalid Command");
        }
      }
    } else {
      shell.getStdErr().add("Invalid Command");
    }
  }


  /**
   * Returns a description of the command
   * 
   * @return The description of the command as a string.
   */
  public static String describeCmd() {
    return "man COMMAND\n"
        + "This command will print out the manual for the specified command.";
  }

}
