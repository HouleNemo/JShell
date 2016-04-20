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

/**
 * The class of all File objects in a mock file system. Files contain a string
 * which represents the contents the file holds.
 * 
 * @author Ming Yi Chan, Chengyi Zhang, Raghavan Chandrabalan, and Hao Chen
 *
 */
public class File {
  private String content;
  private String name;
  private Directory parentDir;

  // Constructor for a blank file.
  /**
   * Constructor for a blank file.
   * @param name - The name of the file.
   * @param currentDir - The parent directory that will contain this file.
   */
  public File(String name, Directory currentDir) {
    this.content = "";
    this.name = name;
    this.parentDir = currentDir;
    parentDir.addFile(this);
  }

  // Erase all contents from this file.
  /**
   * Erase all contents from this file.
   */
  public void erase() {
    this.content = "";
  }

  // Add the argued content to this file.
  /**
   * Append the argued string into the contents of this file.
   * 
   * @param toAdd - The string to be appended.
   */
  public void append(String toAdd) {
    this.content = this.content + toAdd;
  }

  // Get the name of this file.
  /**
   * Get the name of the current file.
   * 
   * @return Returns a string specifying the name of the file.
   */
  public String getName() {
    return this.name;
  }

  // Renames the file.
  /**
   * Changes the name of the file.
   * 
   * @param newName - the new name of the file.
   */
  public void setName(String newName) {
    this.name = newName;
  }

  // Get the parent directory containing this file.
  /**
   * Retrieves the directory containing this file.
   * 
   * @return Returns the parent directory of this file.
   */
  public Directory getParent() {
    return this.parentDir;
  }
  
  // Changes the parent directory.
  /**
   * Changes the parent directory to the specified one.
   * 
   * @param dir - the parent Directory object
   */
  public void setParent(Directory dir) {
    this.parentDir = dir;
  }

  // Get the content contained in this file.
  /**
   * Retrieves the content contained within this file.
   * 
   * @return Returns a string of the contents saved in this file.
   */
  public String getContent() {
    return this.content;
  }
  
  /**
   * Retrieves the path to this file.
   * 
   * @return Returns a string representing the path to the file
   */
  public String getPath() {
    // Removes double slash in path if parent folder is root.
    if(this.parentDir.getPath().equals("/")){
        return "/" + this.name;
    }
    return this.parentDir.getPath() + "/" + this.name;
  }

  // String representation of this file object.
  /**
   * Gives a string representation of the file.
   * 
   * @return Returns a string representation of this file object.
   */
  public String toString() {
    return "File Name: " + this.name + "\nParent Directory: "
        + this.parentDir.getName() + "\nContent:\n" + this.content;
  }
}
