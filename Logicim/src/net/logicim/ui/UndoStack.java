package net.logicim.ui;

import net.logicim.data.circuit.CircuitData;

import java.util.ArrayList;
import java.util.List;

public class UndoStack
{
  protected List<CircuitData> undoStack;

  public UndoStack()
  {
    undoStack = new ArrayList<>();
  }

  public void push(CircuitData circuitData)
  {
    undoStack.add(circuitData);
    System.out.println("UndoStack.push depth [" + undoStack.size() + "]");
  }

  public CircuitData pop()
  {
    if (!undoStack.isEmpty())
    {
      CircuitData circuitData = undoStack.get(undoStack.size() - 1);
      undoStack.remove(undoStack.size() - 1);
      System.out.println("UndoStack.pop depth [" + undoStack.size() + "]");
      return circuitData;
    }
    else
    {
      return null;
    }
  }

  public CircuitData unpop()
  {
    return null;
  }

  public void discard()
  {
    if (!undoStack.isEmpty())
    {
      undoStack.remove(undoStack.size() - 1);
      System.out.println("UndoStack.discard depth [" + undoStack.size() + "]");
    }
  }
}

