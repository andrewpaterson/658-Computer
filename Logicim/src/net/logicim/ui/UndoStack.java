package net.logicim.ui;

import net.logicim.data.circuit.CircuitData;

import java.util.ArrayList;
import java.util.List;

public class UndoStack
{
  protected List<CircuitData> undoStack;
  protected int size;
  protected boolean lastPop;
  protected boolean lastUnpop;

  public UndoStack()
  {
    undoStack = new ArrayList<>();
    size = 0;
    lastPop = false;
    lastUnpop = false;
  }

  public void push(CircuitData circuitData)
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

    undoStack.add(circuitData);
    size++;
    System.out.println("UndoStack.push depth [" + size + ", " + undoStack.size() + "]");
  }

  public CircuitData pop()
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
      CircuitData circuitData = undoStack.get(size - 1);
      size--;
      lastUnpop = false;
      System.out.println("UndoStack.pop depth [" + size + ", " + undoStack.size() + "]");
      return circuitData;
    }
    else
    {
      lastUnpop = false;
      return null;
    }
  }

  public CircuitData unpop()
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
      CircuitData circuitData = undoStack.get(size);
      size++;
      lastPop = false;
      System.out.println("UndoStack.unpop depth [" + size + ", " + undoStack.size() + "]");
      return circuitData;
    }
    else
    {
      lastPop = false;
      return null;
    }
  }
}

