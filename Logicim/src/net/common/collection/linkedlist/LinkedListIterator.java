package net.common.collection.linkedlist;

import java.util.NoSuchElementException;

public class LinkedListIterator<E>
    extends BaseLinkedListIterator<E>
{
  LinkedListIterator(LinkedList<E> list)
  {
    super(list);
    terminalNode = list.getTerminal();
    step = terminalNode.getNext();
  }

  public E next()
  {
    validateModifications();

    if (!hasNext())
    {
      throw new NoSuchElementException();
    }

    E value = step.getValue();
    current = step;

    step = step.getNext();

    return value;
  }

  public void insertBefore(E value)
  {
    LinkedListNode<E> currentNode = getCurrentNode();
    list.addNodeBefore(currentNode, value);
  }
}

