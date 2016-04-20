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
import java.util.Stack;

import driver.*;

/**
 * This is the class for the Cp command. 
 * 
 * @author Ming Yi Chan, Chengyi Zhang, Raghavan Chandrabalan, and Hao Chen
 *
 */

public class Cp {

  /**
   * Copies an item and all subitems of a given path to a new location.
   * 
   * @param shell - A JShell to perform the copy function on.
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
      String errors = copyItem(oldPath,newPath,shell.getWorkingDir());
      if (!errors.isEmpty()) {
        shell.getStdErr().add(errors);
        return;
      }
    }
  }

  /**
   * Checks validity and object type of the parameters 
   * 
   * @param oldPath - The path to the item to copy
   * @param newPath - The destination path
   * @param workingDir - The file system to work in
   * @return A string indicating whether an error occurred or not
   */
  private static String copyItem (String oldPath, String newPath,
      WorkingDir workingDir) {
    
    Stack<WorkingDir> directoryStack = new Stack<WorkingDir>();
    Directory oldParentDir, newParentDir;
    String oldName, newName;
    // Checks the given paths for validity
    oldName = Mv.checkOldPath(oldPath, workingDir, directoryStack);
    if (oldName.isEmpty())
      return "Original path does not lead to a file or directory";
    oldParentDir = workingDir.getCurrentDirectory();
    Popd.popFromStack(workingDir, directoryStack);
    newName = Mv.checkNewPath(newPath, workingDir, directoryStack);
    if (newName.isEmpty())
      return "Destination path does not lead to a file or directory";
    newParentDir = workingDir.getCurrentDirectory();
    Popd.popFromStack(workingDir, directoryStack);
    
    // Checks for three error cases
    if (oldName.charAt(0) == 'd') {
      // A directory cannot be copied to a file
      if (newName.charAt(0) == 'f')
        return "A directory cannot be copied to a file";
      // A directory cannot be copied into one of it's subdirectories
      if ((newParentDir.getPath()+"/").startsWith(
          oldParentDir.getChild(oldName.substring(1)).getPath()+"/"))
        return "A directory cannot be copied into one of its subdirectories";
      // A directory cannot be copied to itself
      if (newName.charAt(0) == 'd') {
        if (newParentDir.getChild(oldName.substring(1)) != null) {
          return "A directory of this name already exists in the destination";
        }
      }
    }
    
    // Performs the actual copy function
    performCopy(oldParentDir, newParentDir, oldName, newName);
    return "";
  }
  
  /**
   * Performs the actual copying of the file or directory depending on the
   * item types the paths point to.
   * 
   * @param oldParentDir - The parent of the original item
   * @param newParentDir - The folder to place the copied item to
   * @param oldName - The name of the original item
   * @param newName - The destination name for the item
   */
  private static void performCopy(Directory oldParentDir,
      Directory newParentDir, String oldName, String newName) {
    
    // Does the copy function according to the given object types
    if (oldName.charAt(0) == 'd') {
      if (newName.charAt(0) == 'n') {
        newParentDir.addDir(
            new Directory(newName.substring(1), newParentDir));
        recursiveCopy(oldParentDir.getChild(oldName.substring(1)),
            newParentDir.getChild(newName.substring(1)));
      }
      else if (newName.charAt(0) == 'd') {
        newParentDir.addDir(
            new Directory(oldName.substring(1), newParentDir));
        recursiveCopy(oldParentDir.getChild(oldName.substring(1)),
            newParentDir.getChild(oldName.substring(1)));
      }
    }
    else if (oldName.charAt(0) == 'f') {
      if (newName.charAt(0) == 'f') {
        String temp = 
            oldParentDir.getFile(oldName.substring(1)).getContent();
        newParentDir.getFile(newName.substring(1)).erase();
        newParentDir.getFile(newName.substring(1)).append(temp);
      }
      else if (newName.charAt(0) == 'n') {
        File temp = new File(newName.substring(1), newParentDir);
        temp.append(oldParentDir.getFile(oldName.substring(1)).getContent());
        newParentDir.addFile(temp);
      }
      else if (newName.charAt(0) == 'd') {
        File temp = new File(oldName.substring(1), newParentDir);
        temp.append(oldParentDir.getFile(oldName.substring(1)).getContent());
        newParentDir.addFile(temp);
      }
    }
  }
  
  /**
   * Recursively copies the subitems of a given directory to a new
   * directory
   * @param copiedDir - The directory that holds the subitems to copy
   * @param destinationDir - The destination for the subitems
   */
  private static void recursiveCopy(Directory copiedDir,
      Directory destinationDir) {
    for (Map.Entry<String, Directory> dir : 
      copiedDir.getAllChildDirs().entrySet()) {
      destinationDir.addDir(
          new Directory(dir.getKey(),destinationDir));
      recursiveCopy(copiedDir.getChild(dir.getKey()),
          destinationDir.getChild(dir.getKey()));
    }
    for (Map.Entry<String, File> file :
      copiedDir.getAllFiles().entrySet()) {
      File temp = new File(file.getKey(),destinationDir);
      temp.append(file.getValue().getContent());
      destinationDir.addFile(temp);
    }
    
  }
  
  /**
   * Returns a description of the command
   * 
   * @return The description string
   */
  public static String describeCmd() {
    return "cp OLDPATH NEWPATH\n"
        + "Will copy the specified file or directory from OLDPATH to\n"
        + "the location specified by NEWPATH.\n"
        + "If NEWPATH is a directory, then the file or directory is\n"
        + "placed in it.\n"
        + "NEWPATH can point to a nonexistant item name to rename the\n"
        + "given item by OLDPATH when copied."
        + "If OLDPATH and NEWPATH point to a file, the destination file\n"
        + "will be overwritten.";
  }
  
}
