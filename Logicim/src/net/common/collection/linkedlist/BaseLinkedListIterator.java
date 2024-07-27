package net.common.collection.linkedlist;

import java.util.ConcurrentModificationException;
import java.util.Iterator;

public abstract class BaseLinkedListIterator<E>
    implements Iterator<E>
{
  protected LinkedList<E> list;
  protected LinkedListNode<E> terminalNode;
  protected LinkedListNode<E> step;
  protected LinkedListNode<E> current;
  protected int modifications;

  public BaseLinkedListIterator(LinkedList<E> list)
  {
    this.list = list;
    this.modifications = list.getModifications();
  }

  public boolean hasNext()
  {
    return step != terminalNode;
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
