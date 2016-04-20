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
import standardStream.*;
import driver.*;

/**
 * This is the class for the History command.
 * 
 * @author Ming Yi Chan, Chengyi Zhang, Raghavan Chandrabalan, and Hao Chen
 *
 */
public class History {


  private List<String> history;


  /**
   * Constructor for the History object.
   */
  public History() {
    this.history = new ArrayList<String>();
  }


  /**
   * Executes the "history" command. Outputs the commands entered into the input
   * JShell object into its StdOut instance.
   * 
   * @param shell - The JShell object on which this command operates.
   */
  public static void execute(JShell shell) {
    if (!shell.getStdIn().isEmpty()) {
      String test = shell.getStdIn().poll();
      if (!shell.getStdIn().isEmpty()) {
        shell.getStdErr().add("Too many parameters.");
        return;
      }
      shell.getStdIn().add(test);
    }
    try {
      shell.getStdOut()
          .add(shell.getShellHistory().getHistory(shell.getStdIn()));
    } catch (NullPointerException e) {
      shell.getStdOut().add(shell.getShellHistory().toString());
    } catch (IllegalArgumentException g) {
      shell.getStdErr().add("Parameter must be a positive integer.");
    }
  }


  /**
   * Adds a string into the history log.
   * 
   * @param toAdd - The string to be added into the history log.
   */
  public void add(String toAdd) {
    history.add(toAdd);
  }


  /**
   * Retrieves the most recent commands entered into JShell as specified by the
   * input in StdIn.
   * 
   * @param stdIn - stdStream object containing an argument which specifies the
   *        number of commands to be output.
   * @return Returns a string of recent commands entered into JShell.
   */
  public String getHistory(StdStream stdIn) {
    // Get argument
    String arg = stdIn.poll();
    // Check argument
    double num = checkArgs(arg);
    // Remove argument from stream
    stdIn.poll();
    // Return recent history based on argument
    return this.getRecent((int) num);
  }


  /**
   * Checks if the input argument is greater than or equal to 0.
   * 
   * @param arg - The argument to be checked.
   * @throws IllegalArgumentException - Thrown if the input argument is not an
   *         integer greater than or equal to 0.
   */
  public static double checkArgs(String arg) throws IllegalArgumentException {
    boolean hasDecimal = false;
    for(int i = 0; i < arg.length(); i++){
      if(!hasDecimal){
        if(!Character.isDigit(arg.charAt(i)) && arg.charAt(i) != '.')
          throw new IllegalArgumentException();
      }
      else{
        if(!Character.isDigit(arg.charAt(i)))
          throw new IllegalArgumentException();
      }
    }
    double num = Double.valueOf(arg);
    if (num < 0 || num != Math.floor(num))
      throw new IllegalArgumentException();
    return num;
  }


  /**
   * Returns the last 'truncate' number of commands entered into JShell.
   * 
   * @param truncate - The number of most recent commands to be returned.
   * @return Returns the last 'truncate' number of entered commands.
   */
  private String getRecent(int truncate) {
    String out = "";
    int numOfRecentCmds = truncate;

    // If requested for more than the amount of history, return everything
    if (numOfRecentCmds > history.size()) {
      numOfRecentCmds = history.size();
    }

    // Format the output
    for (int i = history.size() - numOfRecentCmds; i < history.size(); i++) {
      out = out + Integer.toString(i + 1) + ". " + history.get(i) + "\n";
    }
    return out;
  }
  
  /**
   * Returns a new copy of the history list. 
   */
  public List<String> getList(){
    return new ArrayList<String>(history);
  }
  
  /**
   * Removes all stored commands from the history log.
   */
  public void clear(){
    history.clear();
  }

  /**
   * Returns a string describing what the "History" command does.
   * 
   * @return Returns a string describing the "History" command.
   */
  public static String describeCmd() {
    return "history [number]\n"
        + "This command will print out recent commands, one command per\n"
        + "line (including invalid inputs.) The index 'n' to the left of\n"
        + "each output line shows that the displayed line was the 'nth'\n"
        + "line entered into the shell.\n"
        + "Enter a non-negative integer argument 'm' after the command to\n"
        + "display the 'm' most recent commands.\n";
  }


  /**
   * Returns a string that lists all the commands entered into JShell.
   * 
   * @return Returns a string that lists all the commands entered into JShell.
   */
  public String toString() {
    String out = "";
    for (int i = 0; i < this.history.size(); i++) {
      out = out + Integer.toString(i + 1) + ". " + this.history.get(i) + "\n";
    }
    return out;
  }
}
