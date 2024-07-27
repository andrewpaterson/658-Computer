package net.common.collection.linkedlist;

public class LinkedListNode<E>
{
  private E value;
  private LinkedListNode<E> next;
  private LinkedListNode<E> previous;

  public LinkedListNode(E value)
  {
    this.value = value;
  }

  public LinkedListNode<E> getNext()
  {
    return next;
  }

  public LinkedListNode<E> getPrevious()
  {
    return previous;
  }

  public E getValue()
  {
    return value;
  }

  public void setValue(E value)
  {
    this.value = value;
  }

  protected void remove()
  {
    if (previous != null)
    {
      previous.next = next;
    }

    if (next != null)
    {
      next.previous = previous;
    }
  }

  protected void addBefore(LinkedListNode<E> other)
  {
    if (other == null)
    {
      next = previous = null;
      return;
    }

    next = other;
    previous = other.previous;

    if (other.previous != null)
    {
      other.previous.next = this;
    }

    other.previous = this;
  }

  public void linkToSelf()
  {
    next = this;
    previous = this;
  }

  public void clear()
  {
    value = null;
    next = previous = null;
  }
}

