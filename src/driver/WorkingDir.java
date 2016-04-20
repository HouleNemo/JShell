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

import java.util.NoSuchElementException;

/**
 * The class for the current working directory. Represents the current
 * location in the file system being accessed.
 * 
 * @author Ming Yi Chan, Chengyi Zhang, Raghavan Chandrabalan, and Hao Chen
 *
 */
public class WorkingDir {
  
  private Directory currentDir;
  
  /**
   * Constructor of the working directory object.
   * 
   * @param dir - The directory which shall be set as the current directory.
   */
  public WorkingDir(Directory dir){
    this.currentDir = dir;
  }
  
  /**
   * Returns the path to the current directory.
   * 
   * @return Returns the path to the current directory.
   */
  public String getPath(){
    return this.currentDir.getPath();
  }
  
   /**
    * Returns the directory object of the current working directory.
    * 
    * @return Returns the directory object of the current working directory.
    */
   public Directory getCurrentDirectory(){
     return this.currentDir;
   }
  
  /**
   * Changes the working directory to its parent directory.
   */
  public void changeToParent(){
    this.currentDir = currentDir.getParent();
  }
  
  /**
   * Changes the working directory to a child directory.
   * 
   * @param childName - The name of the target child directory.
   */
  public void changeToChild(String childName){
    Directory temp = this.currentDir;
    this.currentDir = currentDir.getChild(childName);
    // If no child in the current directory has the argued name, throw
    // NoSuchElementException.
    if(this.currentDir == null){
      this.currentDir = temp;
      throw new NoSuchElementException();
    }
  }
  
  /**
   * State representation of the current working directory.
   * 
   * @return Returns a string representation of the current working directory.
   */
  public String toString(){
    return this.currentDir.toString();
  }
  
}
