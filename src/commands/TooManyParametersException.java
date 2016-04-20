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

/**
 * This class represents the exception to be thrown when too many parameters
 * are used to call a command.
 * 
 * @author Ming Yi Chan, Chengyi Zhang, Raghavan Chandrabalan, and Hao Chen
 *
 */
public class TooManyParametersException extends Throwable {
  
  /**
   * Constructor for this exception.
   */
  public TooManyParametersException() {}
  
  /**
   * Constructor for this exception with message display.
   * 
   * @param msg - Message to be displayed when the exception is thrown.
   */
  public TooManyParametersException(String msg){
    super(msg);
  }
  
}
