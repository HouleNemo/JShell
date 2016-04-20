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

import java.util.Arrays;
import java.util.NoSuchElementException;

import driver.*;

/**
 * This is the class for the Cat command.
 * 
 * @author Ming Yi Chan, Chengyi Zhang, Raghavan Chandrabalan, and Hao Chen
 *
 */
public class Cat {


  /**
   * Executes the "cat" command. Prints out the contents of the files whose
   * paths are input into StdIn; one by one.
   * 
   * @param shell - The JShell object on which this command operates.
   */
  public static void execute(JShell shell) {
    String arg = shell.getStdIn().poll();

    // At least one argument expected
    if (arg == null) {
      shell.getStdErr().add("No file name provided.");
      return;
    }

    String path, name;
    String[] fileNames;

    // Loop until the input is fully consumed.
    while (arg != null) {
      path = Cd.getPathFromString(arg);
      name = arg.substring(path.length());

      // Push to a path if one is given
      if (!path.isEmpty()) {
        try {
          Pushd.pushToStack(shell.getWorkingDir(), path,
              shell.getDirectoryStack());
        } catch (NoSuchElementException e) {
          shell.getStdErr().add("Invalid path in " + arg);
          return;
        }
      }

      // Get all files in current directory
      fileNames = shell.getWorkingDir().getCurrentDirectory().getAllFileNames();

      // Checking if a file exists with the given name.
      if (Arrays.asList(fileNames).contains(name)) {
        shell.getStdOut().add(shell.getWorkingDir().getCurrentDirectory()
            .getFile(name).getContent() + "\n");
      } else {
        shell.getStdErr().add("No file with the name " + arg + " exists.\n");
        if (!path.isEmpty()) { // Pops back to the initial directory.
          Popd.popFromStack(shell.getWorkingDir(), shell.getDirectoryStack());
        }
        // return;
      }

      // Pops back to the initial directory
      if (!path.isEmpty()) {
        Popd.popFromStack(shell.getWorkingDir(), shell.getDirectoryStack());
      }

      arg = shell.getStdIn().poll();
      String last = shell.getStdOut().peekLast();
      // Will only print new lines if the next argument is valid and there isn't
      // already new lines.
      if (arg != null && last != null && !last.equals("\n\n\n")) {
        shell.getStdOut().add("\n\n\n");
      }
    }
  }


  /**
   * Returns a string that describes what the "cat" command does.
   * 
   * @return A string describing what the "cat" command does.
   */
  public static String describeCmd() {
    return "cat FILE1 [FILE2 ...]\n"
        + "This command will print out the contents of the file within the\n"
        + "current directory of each name provided after the command. The\n"
        + "contents of each file is separated by three line breaks.\n";
  }

}
