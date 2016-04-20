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

import java.util.Map;
import java.util.TreeMap;

/**
 * The class of all Directory objects in a mock file system. Directories may
 * hold other directories as children and may contain files.
 * 
 * @author Ming Yi Chan, Chengyi Zhang, Raghavan Chandrabalan, and Hao Chen
 *
 */
public class Directory {

  private String name;
  private Directory parentDir;
  private Map<String, Directory> childDirs;
  private Map<String, File> files;

  // Constructor for the root directory. Root is its own parent.
  /**
   * Constructor for a root directory. Root directories are their own parent.
   * 
   * @param name - The name given to this directory.
   */
  public Directory(String name) {
    this.name = name;
    this.parentDir = this;
    this.childDirs = new TreeMap<String, Directory>();
    this.files = new TreeMap<String, File>();
  }

  // Constructor for any other directory.
  /**
   * Constructor for an empty directory.
   * 
   * @param currentDir - The parent directory of this directory.
   * @param name - The name given to the directory to be created.
   */
  public Directory(String name, Directory currentDir) {
    this.name = name;
    this.parentDir = currentDir;
    this.childDirs = new TreeMap<String, Directory>();
    this.files = new TreeMap<String, File>();
    this.parentDir.addDir(this);
  }

  // Add a child into this directory.
  /**
   * Adds another directory into this directory.
   * 
   * @param child - The directory to added within this one.
   */
  public void addDir(Directory child) {
    this.childDirs.put(child.getName(), child);
  }

  // Add a file to this directory.
  /**
   * Adds a file into this directory object.
   * 
   * @param add - The file to be added.
   */
  public void addFile(File add) {
    this.files.put(add.getName(), add);
  }
  
  //Removes a child directory from the directory.
  /**
   * Removes a child directory from the directory
   * 
   * @param name - The name of the directory to be removed.
   */
  public Directory removeChild(String name) {
    return this.childDirs.remove(name);
  }
 
  // Removes a file from the directory.
  /**
   * Removes a file from the directory
   * 
   * @param name - The name of the file to be removed.
   */
  public File removeFile(String name) {
    return this.files.remove(name);
  }
  
  // Get the file with the argued name in this directory.
  /**
   * Get the file with the argued name within this directory.
   * 
   * @param name - The name of the file to be found.
   * @return Returns the file with the argued name.
   */
  public File getFile(String name) {
    return this.files.get(name);
  }

  // Get a the child directory with the argued name in this directory.
  /**
   * Get the child directory with the argued name within this directory.
   * 
   * @param name - The name of the directory to be found.
   * @return Returns the directory with the argued name.
   */
  public Directory getChild(String name) {
    return this.childDirs.get(name);
  }

  // Get the parent of this directory.
  /**
   * Returns the parent directory of this directory object.
   * 
   * @return Returns the parent directory of this directory object.
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

  // Get the name of this directory.
  /**
   * Returns the name of this directory.
   * 
   * @return Returns the name of this directory as a string.
   */
  public String getName() {
    return this.name;
  }

  // Renames the directory.
  /**
   * Changes the name of the directory.
   * @param newName
   */
  public void setName(String newName) {
    this.name = newName;
  }
  
  // Get the path to this directory.
  /**
   * Returns the path of this directory. Returns "/" for the root directory.
   * 
   * @return Returns the path of this directory as a string.
   */
  public String getPath() {
    String path = getPathRecursively();
    if (path.equals("/")) {
      return path;
    }
    else
    {
      // Removes the double slash
      return path.substring(1);
    }
  }
  
  /**
   * Recursively constructs the path to this directory.
   * 
   * @return Returns the path of this directory as a string.
   */
  private String getPathRecursively() {
    if (parentDir.equals(this)) {
      return "/";
    }
    return this.parentDir.getPathRecursively() + "/" + this.name;
  }
  
  /**
   * Get a HashMap of all child directories contained in this directory.
   * 
   * @return Returns a HashMap of all child directories contained in this
   * directory.
   */
  public Map<String, Directory> getAllChildDirs() {
    return this.childDirs;
  }
  
  
  /**
   * Get a HashMap of all files contained in this directory.
   * 
   * @return Returns a HashMap of all files contained in this directory.
   */
  public Map<String, File> getAllFiles() {
    return this.files;
  }

  // String representation of all child Directories
  /**
   * Returns a single string containing the names of all child directories.
   * 
   * @param delim - String that will separate the names of each child in the
   * output string.
   * @return Returns a string containing the names of all child directories.
   */
  public String getAllChildDirsString(String delim){
    String result = "";
    // Get all child directories
    Map<String, Directory> dirMap = this.getAllChildDirs();
    Object[] childArray = dirMap.values().toArray();
    // Get name of each child directories separated by the delimiter
    for (int i = 0; i < childArray.length; i++) {
      result = result + ((Directory)childArray[i]).getName() + delim;
    }
    if ((childArray.length > 0) && !(result.endsWith("\n"))){
      result = result + "\n";
    }
    return result;
  }

  // String representations of all Files
  /**
   * Returns a single string containing the names of all files within this
   * 
   * @param delim - String that will separate the names of each file name in
   * the output string.
   * @return Returns a string containing all names of the files.
   */
  public String getAllFilesString(String delim){
    String result = "";
    // Get all file directories
    Map<String, File> fileMap = this.getAllFiles(); 
    Object[] fileArray = fileMap.values().toArray();
    // Get name of each child directories separated by a tab character
    for (int j = 0; j < fileArray.length; j++) {
      result = result + ((File)fileArray[j]).getName() + delim;
    }
    if ((fileArray.length > 0) && !(result.endsWith("\n"))){
      result = result + "\n";
    }
    return result;
  }

  // String array of all child directory names
  /**
   * Returns an array of strings containing the names of all child directories
   * as entries.
   * 
   * @return Returns an array of strings containing the names of all child
   * directories as entries.
   */
  public String[] getAllChildDirNames(){
    String childDirs = this.getAllChildDirsString("\t");
    childDirs.trim();
    String[] result = childDirs.split("\t");
    return result;
  }

  // String array of all file names in current directory
  /**
   * Returns an array of strings containing the names of all files within this
   * directory as entries.
   * 
   * @return Returns an array of strings containing the names of all files
   * within this directory as entries.
   */
  public String[] getAllFileNames(){
    String childFiles = this.getAllFilesString("\t");
    childFiles.trim();
    String[] result = childFiles.split("\t");
    return result;
  }
  
  /***
   * Returns a boolean indicating if the given directory/file name is
   * valid.
   * 
   * @param name - A string representing the file/directory name to test.
   * @return True if the directory/file name is valid and not
   *    currently taken.
   */
  public boolean isValidName(String name) {
    if (name.isEmpty() || name.equals(".") || name.equals("..") ||
        childDirs.containsKey(name) || files.containsKey(name)) {
      return false;
    }
    return true;
  }

  // State representation of this directory object.
  /**
   * Returns a string representation of this object.
   * 
   * @return Returns a string representation of this object.
   */
  public String toString() {
    String childrenstr = this.getAllChildDirsString("\t");
    String filestr = this.getAllFilesString("\t");
    return "Directory Name: " + this.name + "\nParent Directory: "
        + this.parentDir.getName() + "\nChildren Directories:\n" + childrenstr
        + "Files:\n" + filestr;
  }
}
