package net.logicim.ui;

import java.util.ArrayList;
import java.util.List;

public class Stack<T>
{
  protected List<T> undoStack;
  protected int size;
  protected boolean lastPop;
  protected boolean lastUnpop;

  public Stack()
  {
    clear();
  }

  public void clear()
  {
    undoStack = new ArrayList<>();
    size = 0;
    lastPop = false;
    lastUnpop = false;
  }

  public void push(T t)
  {
    if (lastUnpop)
    {
      size--;
    }

    lastPop = false;
    lastUnpop = true;

    if (size != undoStack.size())
    {
      for (int i = undoStack.size() - 1; i > size; i--)
      {
        undoStack.remove(i);
      }
      size = undoStack.size();
    }

    undoStack.add(t);
    size++;
  }

  public T pop()
  {
    lastPop = true;
    if (size > 0)
    {
      if (lastUnpop)
      {
        if (size > 1)
        {
          size--;
        }
      }
      T circuitData = undoStack.get(size - 1);
      size--;
      lastUnpop = false;
      return circuitData;
    }
    else
    {
      lastUnpop = false;
      return null;
    }
  }

  public T peek()
  {
    if (size > 0)
    {
      if (lastUnpop)
      {
        if (size > 1)
        {
          return undoStack.get(size - 2);
        }
      }
      return undoStack.get(size - 1);
    }
    else
    {
      return null;
    }
  }

  public T unpop()
  {
    lastUnpop = true;
    if (size < undoStack.size())
    {
      if (lastPop)
      {
        if (size < undoStack.size() - 1)
        {
          size++;
        }
      }
      T t = undoStack.get(size);
      size++;
      lastPop = false;
      return t;
    }
    else
    {
      lastPop = false;
      return null;
    }
  }

  public boolean canUnpop()
  {
    return size < undoStack.size();
  }

  public boolean canPop()
  {
    return size > 0;
  }
}

