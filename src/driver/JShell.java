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
package driver;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

import commands.*;
import standardStream.*;

/**
 * The main JShell class.
 * 
 * @author Ming Yi Chan, Chengyi Zhang, Raghavan Chandrabalan, and Hao Chen
 *
 */
public class JShell {

  private String cmd;
  private Scanner in;

  private StdStream stdIn;
  private StdStream stdOut;
  private StdStream stdErr;

  private Directory root;
  private WorkingDir workingDir;

  private History history;
  private Stack<WorkingDir> directoryStack;

  private static JShell JShellRef = null;
  private boolean redirect;

  /**
   * Constructor to instantiate the various components of JShell.
   */
  private JShell() {
    cmd = null;
    stdIn = new StdStream();
    stdOut = new StdStream();
    stdErr = new StdStream();
    root = new Directory("/");
    workingDir = new WorkingDir(root);
    history = new History();
    directoryStack = new Stack<WorkingDir>();
    redirect = false;
  }

  public static JShell createJShell() {
    if (JShellRef == null) {
      JShell res = new JShell();
      JShellRef = res;
      return res;
    }
    return JShellRef;
  }

  /**
   * For a given string stored in cmd, the switchboard method calls the execute
   * method of the corresponding class.
   */
  public void switchBoard() {
    // If using the !number command.
    if (cmd.charAt(0) == '!') {
      Redo.execute(JShellRef);
    }
    // run command based on input
    // Capitalize first letter
    else {
      cmd = cmd.substring(0, 1).toUpperCase() + cmd.substring(1);
      try {
        Class<?> exec = Class.forName("commands." + cmd);
        exec.getMethod("execute", new Class<?>[] {JShell.class}).invoke(exec,
            this);
      } catch (ClassNotFoundException e) {
        stdErr.add("You tried to execute an invalid command. Please try again."
            + "Type \"history\" to see a list of past commands.");
      } catch (IllegalAccessException e) {
        stdErr.add("Error IllegalAccessException");
      } catch (IllegalArgumentException e) {
        stdErr.add("Error IllegalArgumentException");
      } catch (InvocationTargetException e) {
        stdErr.add("Error InvocationTargetException");
      } catch (NoSuchMethodException e) {
        stdErr.add("Invalid command. Please try again.");
      } catch (SecurityException e) {
        stdErr.add("Error SecurityException");
      }
    }
    stdIn.clear();
  }

  /**
   * The prompt method displays the current directory to the user and takes the
   * next input the user enters.
   * 
   * @return Returns a array of string tokens to be used to call commands.
   */
  private String[] prompt() {
    String[] result;
    String input = "";

    // Print prompt
    System.out.print(workingDir.getCurrentDirectory().getPath() + " >");

    // Read input
    input += in.nextLine();

    // Add input to history
    history.add(input);

    // Remove leading and trailing whitespace
    input = input.trim();

    // Split the input into command and arguments
    result = input.split("\\s+(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
    return result;
  }

  /**
   * Clears out any strings left on the input, output, and error streams.
   */
  private void cleanUp() {
    stdIn.clear();
    stdOut.clear();
    stdErr.clear();
  }

  /**
   * Program loop that coordinates the prompt, the switchboard, and the cleanUp
   * methods.
   * 
   * @param inputScanner - Scanner to take user input
   */
  public void runShell(Scanner inputScanner) {

    in = inputScanner;
    cmd = null;

    // Main program loop
    do {
      // Check if the command requires redirection and take the last two param
      // as the marker and path if so.
      String[] lastTwo = checkRedirect();
      // Check for valid command and no error messages
      if (stdErr.isEmpty() && Commands.isValidCmd(cmd)) {
        // Run command via switchboard
        switchBoard();
        // Redirect
        if (redirect)
          Redirect.execute(lastTwo, this);
      } else {
        if (cmd != null && cmd.isEmpty()) {

        } else if ((cmd != null) || Commands.isValidCmd(cmd)) {
          // Check for invalid command, if found write error message
          stdErr.add("Invalid command. Please try again.");
        }

        // Print output and error messages
        System.out.print(stdOut);
        System.out.print(stdErr);

        // Prep for new input
        cleanUp();

        // Get new input, push into StdIn
        stdIn.addAll(prompt());
      }

      // Get next command
      cmd = stdIn.poll();

    } while ((cmd == null) || (cmd != null && !(cmd.equals("exit"))));


  }

  /**
   * Retrieves the input stream object.
   * 
   * @return Returns the input stream object.
   */
  public StdStream getStdIn() {
    return stdIn;
  }

  /**
   * Retrieves the output stream object.
   * 
   * @return Returns the output stream object.
   */
  public StdStream getStdOut() {
    return stdOut;
  }

  /**
   * Retrieves the error stream object.
   * 
   * @return Returns the error stream object.
   */
  public StdStream getStdErr() {
    return stdErr;
  }

  /**
   * Retrieves the current working directory of this JShell object.
   * 
   * @return Returns the working directory of this JShell object.
   */
  public WorkingDir getWorkingDir() {
    return workingDir;
  }

  /**
   * Retrieves the log of all commands entered into this JShell object.
   * 
   * @return Returns the history log of this JShell object.
   */
  public History getShellHistory() {
    return history;
  }

  /**
   * Retrieves the stack of saved directories of this JShell object.
   * 
   * @return Returns the stack of saved directories of this JShell object.
   */
  public Stack<WorkingDir> getDirectoryStack() {
    return directoryStack;
  }

  /**
   * Gets a string representing the command to be executed by JShell.
   */
  public String getCmd() {
    return cmd;
  }

  /**
   * Sets the next command to be executed to the command associated with the
   * input String.
   */
  public void setCmd(String command) {
    cmd = command;
  }

  /**
   * Main method
   * 
   * @param args - Command line arguments
   */
  public static void main(String[] args) {
    // Creates the input scanner
    Scanner mainScanner = new Scanner(System.in);

    // Create JShell
    JShell mainShell = createJShell();
    // Run JShell
    mainShell.runShell(mainScanner);

    // Close scanner
    mainScanner.close();
  }

  /**
   * Return the params for the direction after check if the command requires so
   * 
   * @return The last two elem in stdIn if redirection is required, null
   *         otherwise
   */
  public String[] checkRedirect() {
    // First take the last two elems out
    String[] lastTwo = {stdIn.pollLast(), stdIn.pollLast()};
    // Initiate the output to be the two elems
    String[] res = {lastTwo[1], lastTwo[0]};
    // If the marker is correct, we assume the redirection is called
    if (lastTwo[1] != null) {
      redirect = (lastTwo[1].equals(">") || lastTwo[1].equals(">>"));
    } else {
      redirect = false;
    }
    // If not, set the output to be null and give back the two elems to stdIn
    if (!redirect) {
      if (lastTwo[1] != null) {
        stdIn.add(lastTwo[1]);
      }
      if (lastTwo[0] != null) {
        stdIn.add(lastTwo[0]);
      }
      res = null;
    }
    return res;
  }

}
