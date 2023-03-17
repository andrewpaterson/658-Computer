package net.logicim.ui;

import net.logicim.data.editor.EditorData;

import java.util.ArrayList;
import java.util.List;

public class UndoStack
{
  protected List<EditorData> undoStack;
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

  public void push(EditorData editorData)
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

    undoStack.add(editorData);
    size++;
  }

  public EditorData pop()
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
      EditorData circuitData = undoStack.get(size - 1);
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

  public EditorData unpop()
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
      EditorData editorData = undoStack.get(size);
      size++;
      lastPop = false;
      return editorData;
    }
    else
    {
      lastPop = false;
      return null;
    }
  }
}

