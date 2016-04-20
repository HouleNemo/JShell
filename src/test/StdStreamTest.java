package test;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import standardStream.*;
import java.util.NoSuchElementException;;

public class StdStreamTest {
  
  private StdStream stream;
  
  @Before
  public void setUp() {
    stream = new StdStream();
  }
  
  /**
   * Test whether an element may be added to the stream's queue.
   */
  @Test
  public void addTest() {
    stream.add("Some sample string.");
    assertEquals(stream.poll(),"Some sample string.");
  }
  
  /**
   * Test whether an array of elements may be added to the stream's queue.
   * Elements with the lowest indices are at the front.
   */
  @Test
  public void addAllTest() {
    String[] strings = {"Orchid", "Lily", "Lotus", "Rose"};
    stream.addAll(strings);
    assertEquals(stream.poll(), "Orchid");
    assertEquals(stream.poll(), "Lily");
    assertEquals(stream.poll(), "Lotus");
    assertEquals(stream.poll(), "Rose");
  }
  
  /**
   * Test whether an element may be placed at the front of the stream's queue.
   */
  @Test
  public void addFirstTest(){
    String s1 = "Should be first.";
    String s2 = "Should be second.";
    String s3 = "Should be last.";
    stream.add(s2);
    stream.add(s3);
    stream.addFirst(s1);
    assertEquals(stream.poll(), s1);
    assertEquals(stream.poll(), s2);
    assertEquals(stream.poll(), s3);
  }
  
  /**
   * Test whether a value can be retrieved from the stream queue and whether
   * the queue returns null if it is empty.
   */
  @Test
  public void pollTest(){
    String s = "To be retrieved.";
    stream.add(s);
    assertEquals(stream.poll(), "To be retrieved.");
    assertNull(stream.poll());
  }
  
  /**
   * Test whether a value can be retrieved from the stream queue and whether
   * the queue throws NoSuchElementException if it is empty.
   */
  @Test(expected = NoSuchElementException.class)
  public void removeTest(){
    String s = "To be retrieved.";
    stream.add(s);
    assertEquals(stream.remove(), "To be retrieved.");
    stream.remove();
  }
  
  /**
   * Test whether a value can be retrieved from the stream queue, whether
   * the queue keeps the value, and whether the queue returns null if it is
   * empty.
   */
  @Test
  public void peekTest(){
    String s = "To be peeked.";
    stream.add(s);
    assertEquals(stream.peek(), "To be peeked.");
    assertEquals(stream.poll(), "To be peeked.");
    assertNull(stream.peek());
  }
  
  /**
   * Test whether a value can be retrieved from the stream queue, whether
   * the queue keeps the value, and whether the queue throws a
   * NoSuchElementException if it is empty.
   */
  @Test(expected = NoSuchElementException.class)
  public void elementTest(){
    String s = "To be peeked.";
    stream.add(s);
    assertEquals(stream.element(), "To be peeked.");
    assertEquals(stream.poll(), "To be peeked.");
    stream.element();
  }
  
  /**
   * Test whether the queue can be cleared of all elements.
   */
  @Test
  public void clearTest(){
    String[] s = {"blah", "pizza", "student debt"};
    stream.addAll(s);
    assertEquals(stream.element(), "blah");
    stream.clear();
    assertTrue(stream.isEmpty());
  }
  
  /**
   * Test whether the method returns true if the queue is empty and false
   * otherwise.
   */
  @Test
  public void isEmptyTest(){
    assertNull(stream.poll());
    String s = "Now it's not empty.";
    stream.add(s);
    assertFalse(stream.isEmpty());
    stream.clear();
    assertTrue(stream.isEmpty());
  }

}
