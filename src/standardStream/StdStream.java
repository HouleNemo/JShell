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
package standardStream;

import java.util.*;

public class StdStream {

  private LinkedList<String> stream;

  /**
   * Constructs an empty stream.
   */
  public StdStream() {
    this.stream = new LinkedList<String>();
  }

  /**
   * Adds string str into the stream.
   * 
   * @param str - String to be added.
   */
  public void add(String str) {
    this.stream.add(str);
  }

  /**
   * Adds an array of strings into the stream.
   * 
   * @param str - Array of strings to be added.
   */
  public void addAll(String[] str) {
    this.stream.addAll(Arrays.asList(str));
  }

  /**
   * Adds string str into the beginning of the stream.
   * 
   * @param str - String to be added.
   */
  public void addFirst(String str) {
    this.stream.addFirst(str);
  }

  /**
   * Adds string str into the end of the stream.
   * 
   * @param str - String to be added.
   */
  public void addLast(String str) {
    this.stream.addLast(str);
  }

  /**
   * Retrieves and removes the first string in the stream.
   * 
   * @return first string in the stream, or null if empty
   */
  public String poll() {
    return this.stream.poll();
  }

  /**
   * Retrieves and removes the last string in the stream.
   * 
   * @return last string in the stream, or null if empty
   */
  public String pollLast() {
    return this.stream.pollLast();
  }

  /**
   * Retrieves and removes the first string in the stream.
   * 
   * @return first string in the stream
   * @throws NoSuchElementException if empty
   */
  public String remove() {
    return this.stream.remove();
  }

  /**
   * Retrieves, but does not remove, the first string of the stream.
   * 
   * @return the first string of the stream, or null if empty
   */
  public String peek() {
    return this.stream.peek();
  }

  /**
   * Retrieves, but does not remove, the last string of the stream.
   * 
   * @return the last string of the stream, or null if empty
   */
  public String peekLast() {
    return this.stream.peekLast();
  }

  /**
   * Retrieves, but does not remove, the first string of the stream.
   * 
   * @return the first string of the stream
   * @throws NoSuchElementException if empty
   */
  public String element() {
    return this.stream.element();
  }

  /**
   * Removes all of the strings from this stream.
   */
  public void clear() {
    this.stream.clear();
  }

  /**
   * Deletes the first string in the stream.
   */
  public void deleteFirst() {
    this.stream.poll();
  }

  /**
   * Returns true if the stream is empty.
   * 
   * @return true if the stream is empty
   */
  public boolean isEmpty() {
    return this.stream.size() == 0;
  }

  /**
   * Returns an array containing everything in the stream.
   * 
   * @return an array containing all strings in the stream
   */
  public String[] toArray() {
    return (String[]) this.stream.toArray();
  }

  @Override
  public String toString() {
    String result = "";
    int size = this.stream.size();
    // For every string in the stream
    for (int i = 0; i < size; i++) {
      // add it to result
      result = result + this.stream.get(i);
      // If it does not end in a new line character
      try {
        if (!(this.stream.get(i).endsWith("\n"))) {
          // add it in
          result = result + "\n";
        }
      } catch (NullPointerException e) {
        return "";
      }
    }
    return result;
  }

  public String toString(String delim) {
    String result = "";
    int size = this.stream.size();
    if (size > 0) {
      // For every string in the stream
      for (int i = 0; i < size - 1; i++) {
        // add it to result
        result = result + this.stream.get(i) + delim;
      }
      result = result + this.stream.get(size - 1);
    }
    return result + "\n";
  }
}
