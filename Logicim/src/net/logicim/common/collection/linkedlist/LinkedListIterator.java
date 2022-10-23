package net.logicim.common.collection.linkedlist;

import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

class LinkedListIterator<E>
    implements Iterator<E>
{
  private LinkedList<E> list;
  private LinkedListNode<E> root;
  private LinkedListNode<E> next;
  private LinkedListNode<E> current;
  private int modifications;

  LinkedListIterator(LinkedList<E> list)
  {
    this.list = list;
    root = list.getRoot();
    next = root.getNext();
    this.modifications = list.getModifications();
  }

  public boolean hasNext()
  {
    return next != root;
  }

  public E next()
  {
    validateModifications();

    if (!hasNext())
    {
      throw new NoSuchElementException();
    }

    E value = next.getValue();
    current = next;

    next = next.getNext();

    return value;
  }

  public void remove()
  {
    validateModifications();

    LinkedListNode<E> node = getCurrentNode();
    list.removeNode(node);

    current = null;
    modifications++;
  }

  protected LinkedListNode<E> getCurrentNode() throws IllegalStateException
  {
    if (current == null)
    {
      throw new IllegalStateException();
    }

    return current;
  }

  protected void validateModifications()
  {
    if (list.getModifications() != modifications)
    {
      throw new ConcurrentModificationException();
    }
  }
}

