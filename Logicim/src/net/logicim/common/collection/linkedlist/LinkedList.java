package net.logicim.common.collection.linkedlist;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class LinkedList<E>
    implements
    Iterable<E>
{
  protected LinkedListNode<E> terminal;
  protected transient int modifications;

  public LinkedList()
  {
    baseClear();
  }

  public static boolean safeEquals(Object lhsOrNull, Object rhsOrNull)
  {
    if (lhsOrNull == null && rhsOrNull == null)
    {
      return true;
    }

    if (lhsOrNull == null || rhsOrNull == null)
    {
      return false;
    }

    if (lhsOrNull == rhsOrNull)
    {
      return true;
    }

    return lhsOrNull.equals(rhsOrNull);
  }

  protected void baseClear()
  {
    modifications = 0;
    terminal = createRoot();
  }

  protected LinkedListNode<E> createRoot()
  {
    LinkedListNode<E> result = createNode(null);

    result.linkToSelf();

    return result;
  }

  // Getters
  public E getFirstOrNull()
  {
    LinkedListNode<E> result = terminal.getNext();
    if (terminal == result)
    {
      return null;
    }
    return result.getValue();
  }

  public E getLastOrNull()
  {
    LinkedListNode<E> result = terminal.getPrevious();
    if (terminal == result)
    {
      return null;
    }
    return result.getValue();
  }

  public int size()
  {
    int result = 0;
    for (E e : this)
    {
      result++;
    }
    return result;
  }

  public boolean isEmpty()
  {
    return terminal.getNext() == terminal;
  }

  public LinkedListNode<E> getTerminal()
  {
    return terminal;
  }

  public LinkedListNode<E> getTail()
  {
    return terminal.getPrevious();
  }

  public LinkedListNode<E> getHead()
  {
    return terminal.getNext();
  }

  public int getModifications()
  {
    return modifications;
  }

  protected void addNodeBefore(LinkedListNode<E> node, E value)
  {
    LinkedListNode<E> insert = createNode(value);
    insert.addBefore(node);

    modifications++;
  }

  protected boolean baseAdd(E value)
  {
    addNodeBefore(terminal, value);
    return true;
  }

  private boolean baseRemove(Iterator<E> iterator, E value)
  {
    while (iterator.hasNext())
    {
      Object each = iterator.next();

      if (safeEquals(value, each))
      {
        iterator.remove();
        return true;
      }
    }

    return false;
  }

  protected E removeNode(LinkedListNode<E> node)
  {
    E result = node.getValue();

    node.remove();

    modifications++;

    return result;
  }

  protected boolean baseRemoveAll(Collection<?> values)
  {
    boolean result = false;

    for (Iterator<?> iterator = iterator(); iterator.hasNext(); )
    {
      Object each = iterator.next();

      if (values.contains(each))
      {
        iterator.remove();
        result = true;
      }
    }

    return result;
  }

  protected boolean baseRetainAll(Collection<?> values)
  {
    boolean result = false;

    for (Iterator iterator = iterator(); iterator.hasNext(); )
    {
      Object each = iterator.next();

      if (!values.contains(each))
      {
        iterator.remove();
        result = true;
      }
    }

    return result;
  }

  public LinkedListIterator<E> iterator()
  {
    return new LinkedListIterator<E>(this);
  }

  public boolean add(E value)
  {
    return baseAdd(value);
  }

  public boolean addAll(Collection<? extends E> values)
  {
    for (E each : values)
    {
      baseAdd(each);
    }

    return true;
  }

  public void clear()
  {
    baseClear();
  }

  public boolean removeAll(Collection<?> values)
  {
    return baseRemoveAll(values);
  }

  public boolean retainAll(Collection<?> values)
  {
    return baseRetainAll(values);
  }

  public boolean remove(E value)
  {
    return baseRemove(iterator(), value);
  }

  public List<E> toList()
  {
    ArrayList<E> result = new ArrayList<>();
    for (E e : this)
    {
      result.add(e);
    }
    return result;
  }

  protected LinkedListNode<E> createNode(E value)
  {
    return new LinkedListNode<E>(value);
  }
}

