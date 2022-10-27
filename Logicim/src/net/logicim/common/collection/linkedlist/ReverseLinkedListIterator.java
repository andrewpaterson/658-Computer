package net.logicim.common.collection.linkedlist;

import java.util.NoSuchElementException;

public class ReverseLinkedListIterator<E>
    extends BaseLinkedListIterator<E>
{
  ReverseLinkedListIterator(LinkedList<E> list)
  {
    super(list);
    terminalNode = list.getTail();
    step = terminalNode.getPrevious();
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

    step = step.getPrevious();

    return value;
  }
}

