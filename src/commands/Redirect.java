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

import java.util.Map;
import java.util.NoSuchElementException;

import driver.*;

/**
 * This is the class for the Redirect application.
 * 
 * @author Ming Yi Chan, Chengyi Zhang, Raghavan Chandrabalan, and Hao Chen
 *
 */
public class Redirect {

  /**
   * Execute the application "> Outfile". Use it to redirect the stdOut of a
   * command to build, cover, or append an Outfile
   * 
   * @param shell - The main shell in which this command is called.
   * @param lastTwo - The array of string consisting of the last two elems of
   *        the input
   */
  public static void execute(String[] lastTwo, JShell shell) {
    String name = processArguments(lastTwo, shell);
    String content = shell.getStdOut().toString().trim();
    String marker = lastTwo[0];
    if (!name.isEmpty()) {
      writeToFile(name, content, marker, shell);
      // Finally, use Popd to return to the original folder of where the
      // command is called.
      Popd.execute(shell);
    }
    shell.getStdOut().clear();
  }

  /**
   * Takes the arguments from echo and checks for validity. It then changes to
   * the relevant directory for the input and returns the name of the output
   * file separated from the path.
   * 
   * @param marker - Expected to be ">" or ">>" which indicates whether echo
   *        will overwrite or append.
   * @param shell - The JShell that contains the relevant file system, provides
   *        any following inputs and will handle any possible errors.
   * @return A string representing the name of the output file. Will be an empty
   *         string if an error occurs.
   */
  private static String processArguments(String[] lastTwo, JShell shell) {
    String marker = lastTwo[0];
    // If marker is not ">" nor ">>", print an error.
    if (!marker.equals(">") && !marker.equals(">>")) {
      shell.getStdErr()
          .add("The marker in front of address must be \">\" or \">>\".");
      return "";
      // If marker is valid without the aim file to operate, print an error.
    } else if (lastTwo[1] == null) {
      shell.getStdErr()
          .add("Please indicate the address of the target file for the input.");
      return "";
    } else {
      // Take the third input as the prePath, which will be split into a
      // path and a file name.
      String prePath = lastTwo[1];
      // Get the file path and name.
      String path = Cd.getPathFromString(prePath);
      String name = prePath.substring(path.length());
      if (name.isEmpty()) {
        shell.getStdErr().add("Invalid file name.");
        return "";
      }
      // Push to the chosen directory
      try {
        Pushd.pushToStack(shell.getWorkingDir(), path,
            shell.getDirectoryStack());
      } catch (NoSuchElementException e) {
        shell.getStdErr().add("Invalid file path.");
        return "";
      }
      return name;
    }
  }

  /**
   * Will take content and write it to an existing or new file of the given
   * name.
   * 
   * @param name - The name of the file to write to.
   * @param content - The string which will be written.
   * @param marker - The option for whether echo appends or overwrites.
   * @param shell - The JShell that contains the relevant file system and will
   *        take in the output.
   */
  private static void writeToFile(String name, String content, String marker,
      JShell shell) {
    // Then deal with echo in that folder.
    Map<String, File> f =
        shell.getWorkingDir().getCurrentDirectory().getAllFiles();
    String fileName = name;
    if (f.containsKey(fileName)) {
      if (marker.equals(">")) {
        f.get(fileName).erase();
        f.get(fileName).append(content);
      } else if (marker.equals(">>")) {
        f.get(fileName).append("\n" + content);
      }
    } else {
      if (!shell.getWorkingDir().getCurrentDirectory().isValidName(name)) {
        shell.getStdErr().add("The file name: \"" + name + "\" is invalid.");
        return;
      }
      File newFile =
          new File(fileName, shell.getWorkingDir().getCurrentDirectory());
      newFile.append(content);
    }
  }
}
