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

import driver.*;
import commands.MissingQuotesException;
import java.util.Scanner;
import java.util.Map;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * The class representing the grep command.
 * 
 * @author Ming Yi Chan, Chengyi Zhang, Raghavan Chandrabalan, and Hao Chen
 *
 */
public class Grep {
  
  /**
   * Execute the grep command.
   * 
   * @param shell - The main shell in which this command is called.
   */
  public static void execute(JShell shell){
    Directory tempDir = shell.getWorkingDir().getCurrentDirectory();
    try{
      // Store parameters.
      String expression = shell.getStdIn().poll();
      String path = "";
      
      // Recursive flag.
      if(expression.equals("-R") || expression.equals("-r")){
        expression = shell.getStdIn().poll();
        checkExpression(expression);
        expression = expression.substring(1, expression.length()-1);
        path = shell.getStdIn().poll();
        // Ensure there are no more parameters.
        if(shell.getStdIn().poll() != null)
          throw new TooManyParametersException();
        recurseInDirectories(shell, path, expression, tempDir);
      }
      // Non-recursive case.
      else{
        checkExpression(expression);
        expression = expression.substring(1, expression.length()-1);
        path = shell.getStdIn().poll();
        // Ensure there are no more parameters.
        if(shell.getStdIn().poll() != null)
          throw new TooManyParametersException();
        searchInFile(shell, path, expression, tempDir, false);
      }
    }
    catch(MissingQuotesException e){
      shell.getStdErr().add("The input string needs to be surrounded by double"
          + " quotation marks.");
    }
    catch(NullPointerException f){
      shell.getStdErr().add("File with the input path does not exist.");
    }
    catch(NoSuchElementException g){
      shell.getStdErr().add("Directory with the input path does not exist.");
    }
    catch(TooManyParametersException h){
      shell.getStdErr().add("Too many parameters.");
    }
    // Restore original directory.
    Cd.changeDirectory(shell.getWorkingDir(), tempDir.getPath());
  }
  
  /**
   * Continuously changes working directories and searches for the input REGEX
   * in all files within them until all subdirectories have been traversed and
   * searched.
   * 
   * @param shell - The main shell in which this command is called.
   * @param path - The path to the file from which the recursion starts.
   * @param expression - The REGEX to be found.
   * @param tempDir - The directory to return to after the current iteration.
   */
  private static void recurseInDirectories(JShell shell, String path,
      String expression, Directory tempDir){
    Cd.changeDirectory(shell.getWorkingDir(), path);
    Map<String, Directory> childDirs = shell.getWorkingDir()
        .getCurrentDirectory().getAllChildDirs();
    Iterator<String> i = childDirs.keySet().iterator();
    String currentPath = "";
    // Recurse this method for each child directory to the directory at the
    // input path.
    while(i.hasNext()){
      currentPath = childDirs.get(i.next()).getPath();
      recurseInDirectories(shell, currentPath, expression, shell.getWorkingDir()
          .getCurrentDirectory());
    }
    searchInAllFiles(shell, expression, tempDir);
    Cd.changeDirectory(shell.getWorkingDir(), tempDir.getPath());
  }
  
  /**
   * Searches for the input REGEX in all files of the current working directory.
   * 
   * @param shell - The main shell in which this command is called.
   * @param expression - The REGEX to be found.
   * @param tempDir - The directory to return to after each search.
   */
  private static void searchInAllFiles(JShell shell, String expression,
      Directory tempDir){
    Map<String, File> files = shell.getWorkingDir().getCurrentDirectory()
        .getAllFiles();
    Iterator<String> i = files.keySet().iterator();
    while(i.hasNext()){
      searchInFile(shell, files.get(i.next()).getPath(), expression, tempDir,
          true);
    }
  }
  
  /**
   * Searches for the input REGEX in the file with the given path.
   * 
   * @param shell - The main shell in which this command is called.
   * @param path - The path of the file to be searched.
   * @param expression - The REGEX to be found.
   * @param tempDir - The directory to return to after the search.
   * @param header - If true, prints a header to the searched file with a colon
   * before non-trivial output.
   */
  private static void searchInFile(JShell shell, String path, String expression,
      Directory tempDir, boolean header){
    String parentPath = Cd.getPathFromString(path);
    if(parentPath != null)
      Cd.changeDirectory(shell.getWorkingDir(), parentPath);
    // Retrieve the file at the given path.
    File file = shell.getWorkingDir().getCurrentDirectory()
        .getFile(path.substring(path.lastIndexOf("/")+1));
    Scanner in = new Scanner(file.getContent());
    String line = "";
    String result = "";
    // Scan each line in the file to see if it contains the REGEX.
    while (in.hasNextLine()){
      line = in.nextLine();
      if(line.contains(expression))
        result = result + line + "\n"; // If it does, add it to the result.
    }
    // For non-trivial outputs, print the header during the recursive case.
    if(!result.equals("")){
      if(header)
        result = file.getPath() + ":\n" + result + "\n";
      shell.getStdOut().add(result);
    }
    in.close();
    Cd.changeDirectory(shell.getWorkingDir(), tempDir.getPath());
  }
  
  /**
   * Checks whether the expression to be found is within double quotation marks.
   * If not, throw an exception.
   * 
   * @param expression - The REGEX to be checked.
   * @throws MissingQuotesException - Thrown if the REGEX provided is not
   * within double quotation marks.
   */
  private static void checkExpression(String expression) throws
    MissingQuotesException {
    if (!expression.startsWith("\"") || !expression.endsWith("\""))
      throw new MissingQuotesException();
  }
  
  /**
   * Returns the description of the command "grep."
   * 
   * @return Returns a string describing the command "grep."
   */
  public static String describeCmd() {
    return "grep [-R] REGEX PATH:\n"
        + "Without the parameter -R, this command prints any lines containing\n"
        + "REGEX in the file found at PATH.\n"
        + "If the parameter -R is provided, this command recursively prints\n"
        + "any lines containing REGEX in any file within the subdirectories\n"
        + "of the directory at PATH.";
  }
}
