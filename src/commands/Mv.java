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
import java.util.Stack;

import driver.*;

/**
 * This is the class for the Mv command. 
 * 
 * @author Ming Yi Chan, Chengyi Zhang, Raghavan Chandrabalan, and Hao Chen
 *
 */

public class Mv {

  /**
   * Moves an item and, by extension, all subitems of a given path 
   * to a new location.
   * 
   * @param shell - A JShell to perform the move function on.
   */
  public static void execute(JShell shell) {
    // Takes the first parameter as the old path
    String oldPath = shell.getStdIn().poll();
    // Second parameter as the new path
    String newPath = shell.getStdIn().poll();
    // Checks for an error in the input
    if (!shell.getStdIn().isEmpty())
      shell.getStdErr().add("Too many parameters");
    else if(newPath == null)
      shell.getStdErr().add("Missing parameters");
    else {
      // Performing the function
      String errors = moveItem(oldPath,newPath,shell.getWorkingDir());
      if (!errors.isEmpty())
        shell.getStdErr().add(errors);
    }
  }

  /**
   * Checks validity and object type of the parameters 
   * 
   * @param oldPath - The path to the item to move
   * @param newPath - The destination path
   * @param workingDir - The file system to work in
   * @return A string indicating whether an error occurred or not
   */
  private static String moveItem (String oldPath, String newPath,
      WorkingDir workingDir) {
    
    Stack<WorkingDir> directoryStack = new Stack<WorkingDir>();
    Directory oldParentDir, newParentDir;
    String oldName, newName;
    // Checks the given paths for validity
    oldName = checkOldPath(oldPath, workingDir, directoryStack);
    if (oldName.isEmpty())
      return "Original path does not lead to a file or directory";
    oldParentDir = workingDir.getCurrentDirectory();
    Popd.popFromStack(workingDir, directoryStack);
    newName = checkNewPath(newPath, workingDir, directoryStack);
    if (newName.isEmpty())
      return "Destination path does not lead to a file or directory";
    newParentDir = workingDir.getCurrentDirectory();
    Popd.popFromStack(workingDir, directoryStack);
    
    // Checks for two error cases
    if (oldName.charAt(0) == 'd') {
      // A directory cannot move into a file
      if (newName.charAt(0) == 'f')
        return "A directory cannot be moved into a file";
      // A directory cannot move into one of it's subdirectories
      if (newParentDir.getPath().startsWith(
          oldParentDir.getChild(oldName.substring(1)).getPath()))
        return "A directory cannot move into one of its subdirectories";
    }
    // Performs the actual move function
    performMove(oldParentDir, newParentDir, oldName, newName);
    return "";
  }
  
  /**
   * Checks if the original path points to an existing file or directory
   * Sets the workingDir to the parent of the item to move.
   * 
   * @param oldPath - The path to the item
   * @param workingDir - The file system to work in
   * @param directoryStack - The stack to store the original position of
   *    workingDir to.
   * @return An error string
   */
  protected static String checkOldPath(String oldPath, WorkingDir workingDir,
      Stack<WorkingDir> directoryStack) {
    String name;
    // Checks if oldPath exists
    try {
      Pushd.pushToStack(workingDir, oldPath, directoryStack);
      name = "d" + workingDir.getCurrentDirectory().getName();
      workingDir.changeToParent();
    } catch (NoSuchElementException e) {
      // Check if the path leads to a file
      String path = Cd.getPathFromString(oldPath);
      name = oldPath.substring(path.length());
      
      if (name.isEmpty() || name.equals("..") || name.equals("."))
        return "";
      try {
        Pushd.pushToStack(workingDir, path, directoryStack);
      } catch (NoSuchElementException f) {
        return "";
      }
      if (workingDir.getCurrentDirectory().getFile(name) == null) {
        Popd.popFromStack(workingDir, directoryStack);
        return "";
      }
      name = "f" + name;
    }
    return name;
  }
  
  /**
   * Checks if the destination path points to an existing file or directory
   * Sets the workingDir to the directory the item is to move into.
   * 
   * @param newPath - The destination path
   * @param workingDir - The file system to work in
   * @param directoryStack - The stack to store the original position of
   *    workingDir to.
   * @return An error string
   */
  protected static String checkNewPath(String newPath, WorkingDir workingDir,
      Stack<WorkingDir> directoryStack) {
    String name;
    // Checks if newPath is valid
    try {
      Pushd.pushToStack(workingDir, newPath, directoryStack);
      name = "d" + workingDir.getCurrentDirectory().getName();
    } catch (NoSuchElementException e) {
      // Check if the path leads to a file
      String path = Cd.getPathFromString(newPath);
      name = newPath.substring(path.length());
      
      if (name.isEmpty() || name.equals("..") || name.equals("."))
        return "";
      try {
        Pushd.pushToStack(workingDir, path, directoryStack);
      } catch (NoSuchElementException f) {
        return "";
      }
      if (workingDir.getCurrentDirectory().getFile(name) == null) {
        name = "n" + name;
      }
      else {
        name = "f" + name;
      }
    }
    return name;
  }
  
  /**
   * Performs the actual moving of the file or directory depending on the
   * item types the paths point to.
   * 
   * @param oldParentDir - The parent of the original item
   * @param newParentDir - The folder to place the moved item to
   * @param oldName - The name of the original item
   * @param newName - The destination name for the item
   */
  private static void performMove(Directory oldParentDir,
      Directory newParentDir, String oldName, String newName) {
    
    // Does the move function according to the given object types
    if (oldName.charAt(0) == 'd') {
      if (newName.charAt(0) == 'n') {
        Directory temp = oldParentDir.removeChild(oldName.substring(1));
        temp.setName(newName.substring(1));
        temp.setParent(newParentDir);
        newParentDir.addDir(temp);
      }
      else if (newName.charAt(0) == 'd') {
        newParentDir.addDir(oldParentDir.removeChild(oldName.substring(1)));
        newParentDir.getChild(oldName.substring(1)).setParent(newParentDir);
      }
    }
    else if (oldName.charAt(0) == 'f') {
      if (newName.charAt(0) == 'f') {
        File temp = oldParentDir.removeFile(oldName.substring(1));
        newParentDir.removeFile(newName.substring(1));
        temp.setName(newName.substring(1));
        temp.setParent(newParentDir);
        newParentDir.addFile(temp);
      }
      else if (newName.charAt(0) == 'n') {
        File temp = oldParentDir.removeFile(oldName.substring(1));
        temp.setName(newName.substring(1));
        temp.setParent(newParentDir);
        newParentDir.addFile(temp);
      }
      else if (newName.charAt(0) == 'd') {
        newParentDir.addFile(oldParentDir.removeFile(oldName.substring(1)));
        newParentDir.getFile(oldName.substring(1)).setParent(newParentDir);
      }
    }
  }
  
  /**
   * Returns a description of the command
   * 
   * @return The description string
   */
  public static String describeCmd() {
    return "mv OLDPATH NEWPATH\n"
        + "Will move the specified file or directory from OLDPATH to\n"
        + "the location specified by NEWPATH.\n"
        + "If NEWPATH is a directory, then the file or directory is\n"
        + "placed in it.\n"
        + "NEWPATH can point to a nonexistant item name to rename the\n"
        + "given item by OLDPATH."
        + "If OLDPATH and NEWPATH point to a file, the destination file\n"
        + "will be overwritten.";
  }
}
