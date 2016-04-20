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

import java.util.NoSuchElementException;
import java.util.Map;
import java.util.TreeMap;
import java.util.Iterator;

import driver.*;

/**
 * This is class for the Ls command.
 * 
 * @author Ming Yi Chan, Chengyi Zhang, Raghavan Chandrabalan, and Hao Chen
 *
 */
public class Ls {

  /**
   * Executes the Ls command. Lists all the directories and files found in the
   * given path into StdOut. If the given path specifies a file, output the path
   * into StdOut. If the -R flag is inputted, lists all subdirectories and
   * subfiles of the given path recursively.
   * 
   * @param shell - The JShell object on which this command operates.
   */
  public static void execute(JShell shell) {
    String path = shell.getStdIn().poll();
    Directory tempDir = shell.getWorkingDir().getCurrentDirectory();
    boolean noRDisplayHeader = true;
    try {
      try {
        // If recursive case...
        if (path.equals("-R") || path.equals("-r")) {
          // ...always display full path.
          recursiveLs(shell, path, tempDir);
          // Process the next path provided if one exists.
          if(shell.getStdIn().peek() != null){
            shell.getStdIn().addFirst("-R");
            execute(shell);
          }
          return;
        }
      }
      // If only "ls" was called with no arguments.
      catch (NullPointerException e) {
        path = shell.getWorkingDir().getCurrentDirectory().getPath();
        noRDisplayHeader = false; // Don't use header for this case.
      }
      nonRecursiveLs(shell, path, noRDisplayHeader, tempDir);
      // Process the next path provided if one exists.
      if(shell.getStdIn().peek() != null)
        execute(shell);
    } catch (NoSuchElementException e) {
      // Cd would have moved the working directory to the farthest valid
      // directory of the input path before error was thrown.
      // Attempt to find the file at the input path (absolute and relative.)
      String endPath1 = path.replace(shell.getWorkingDir().getPath() + "/", "");
      String endPath2 =
          path.replace(shell.getWorkingDir().getPath().substring(1) + "/", "");
      if (shell.getWorkingDir().getCurrentDirectory().getAllFiles()
          .containsKey(endPath1))
        shell.getStdOut().add(endPath1 + "\n\n");
      else if (shell.getWorkingDir().getCurrentDirectory().getAllFiles()
          .containsKey(endPath2))
        shell.getStdOut().add(endPath2 + "\n\n");
      else // No file or directory at input path -> error.
        shell.getStdErr().add(
            "Directory or File with the given path " + "does not exist.\n");
      // Change back to original directory.
      Cd.changeDirectory(shell.getWorkingDir(), tempDir.getPath());
    }
  }

  /**
   * Manages the non recursive ls command. Changes the working directory to the
   * one with the input path and outputs the names of its files and child
   * directories into StdOut. Then restores the working directory to before ls
   * was called.
   * 
   * @param shell - The JShell object on which this command operates.
   * @param path - The path of the directory whose contents we wish to display.
   * @param noRDisplayHeader - If true, prints the name of the directory at the
   *        input path with a colon as a header for the output.
   * @param tempDir - The directory to restore once the Ls command terminates.
   */
  private static void nonRecursiveLs(JShell shell, String path,
      boolean noRDisplayHeader, Directory tempDir) {
    Cd.changeDirectory(shell.getWorkingDir(), path);
    String header = shell.getWorkingDir().getCurrentDirectory().getName() + ":";
    displayDirs(shell);
    displayFiles(shell, false);
    if (noRDisplayHeader)
      shell.getStdOut().addFirst(header);
    // Change back to original directory.
    Cd.changeDirectory(shell.getWorkingDir(), tempDir.getPath());
  }

  /**
   * Manages the recursive ls command. Changes the working directory to the one
   * with the input path and recursively outputs the names of all its files and
   * subdirectories into StdOut. Then restores the working directory to before
   * ls was called.
   * 
   * @param shell - The JShell object on which this command operates.
   * @param path - The path of the directory whose contents we wish to display.
   * @param tempDir - The directory to return to once the Ls command terminates.
   */
  private static void recursiveLs(JShell shell, String path,
      Directory tempDir) {
    String header = "";
    // If recursive ls with arguments, use current working directory
    // path as header.
    if (shell.getStdIn().peek() != null) {
      path = shell.getStdIn().poll();
      Cd.changeDirectory(shell.getWorkingDir(), path);
      header = shell.getWorkingDir().getPath() + ":";
    }
    recurseDisplay(shell, true); // Recurse file reading.
    // If recursive ls -R with no arguments, do not use header.
    if (!header.equals(""))
      shell.getStdOut().addFirst(header);
    // Change back to original directory.
    Cd.changeDirectory(shell.getWorkingDir(), tempDir.getPath());
  }

  /**
   * Recursively outputs the name of the contents in the subdirectories of the
   * current working directory into StdOut.
   * 
   * @param shell - The JShell object on which this command operates.
   * @param fullPath - Determines if the full path should be printed or just the
   *        relative path.
   */
  private static void recurseDisplay(JShell shell, boolean fullPath) {
    Map<String, Directory> childDirs =
        ((TreeMap<String, Directory>) shell.getWorkingDir()
            .getCurrentDirectory().getAllChildDirs()).descendingMap();
    Iterator<String> iChild = childDirs.keySet().iterator();
    String currentPath = "";
    Directory currentDir = shell.getWorkingDir().getCurrentDirectory();
    // For each child directory, recurse this method.
    while (iChild.hasNext()) {
      currentPath = childDirs.get(iChild.next()).getPath();
      Cd.changeDirectory(shell.getWorkingDir(), currentPath);
      recurseDisplay(shell, fullPath);
      shell.getStdOut().addFirst(shell.getWorkingDir().getPath());
      Cd.changeDirectory(shell.getWorkingDir(), currentDir.getPath());
    }
    displayFiles(shell, fullPath);
  }

  /**
   * Outputs the names of the files within the current working directory into
   * StdOut.
   * 
   * @param shell - The JShell object on which this command operates.
   * @param fullPath - Determines if the full path should be printed or just the
   *        relative path.
   */
  private static void displayFiles(JShell shell, boolean fullPath) {
    String result = "";
    Map<String, File> childFiles =
        shell.getWorkingDir().getCurrentDirectory().getAllFiles();
    Iterator<String> iFile = childFiles.keySet().iterator();
    while (iFile.hasNext()) {
      if (fullPath)
        result += childFiles.get(iFile.next()).getPath() + "\n";
      else
        result += childFiles.get(iFile.next()).getName() + "\n";
    }
    if (!result.equals(""))
      shell.getStdOut().addFirst(result);
  }

  /**
   * Outputs the names of the child directories within the current working
   * directory into StdOut.
   * 
   * @param shell - The JShell object on which this command operates.
   * @param fullPath - Determines if the full path should be printed or just the
   *        relative path.
   */
  private static void displayDirs(JShell shell) {
    String result = "";
    Map<String, Directory> childDirs =
        shell.getWorkingDir().getCurrentDirectory().getAllChildDirs();
    Iterator<String> iChild = childDirs.keySet().iterator();
    while (iChild.hasNext())
      result += childDirs.get(iChild.next()).getName() + "\n";
    if (!result.equals(""))
      shell.getStdOut().addFirst(result);
  }


  /**
   * Returns a string that describes what the "ls" command does.
   * 
   * @return Returns a string that describes what the "ls" command does.
   */
  public static String describeCmd() {
    return "ls [PATH ...]\n"
        + "Without any parameters, this command lists all child directories\n"
        + "and files in the current working directory. If the command is\n"
        + "followed by a path to a directory, the command lists the contents\n"
        + "of that directory. If the path leads to a file, the path itself\n"
        + "is printed.\n"
        + "If the command is followed by '-R', this command recursively lists\n"
        + "all subdirectories and files from the target directory.";
  }

}
