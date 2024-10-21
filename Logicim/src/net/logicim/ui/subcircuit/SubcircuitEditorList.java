package net.logicim.ui.subcircuit;

import net.common.SimulatorException;
import net.common.util.StringUtil;
import net.logicim.ui.circuit.SubcircuitInstanceViewFinder;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.simulation.component.subcircuit.SubcircuitInstanceView;
import net.logicim.ui.simulation.subcircuit.SubcircuitEditor;

import java.util.ArrayList;
import java.util.List;

public class SubcircuitEditorList
    implements SubcircuitInstanceViewFinder
{
  protected List<SubcircuitEditor> subcircuitEditors;
  protected SubcircuitEditor currentSubcircuitEditor;
  protected List<SubcircuitListChangedListener> subcircuitListChangedListeners;

  public SubcircuitEditorList()
  {
    this.subcircuitEditors = new ArrayList<>();
    this.currentSubcircuitEditor = null;
    this.subcircuitListChangedListeners = new ArrayList<>();
  }

  public void clear(boolean notifyChange)
  {
    subcircuitEditors = new ArrayList<>();
    notifySubcircuitListChanged(notifyChange);
    currentSubcircuitEditor = null;
  }

  public void add(SubcircuitEditor subcircuitEditor, boolean notifyChange)
  {
    subcircuitEditors.add(subcircuitEditor);
    notifySubcircuitListChanged(notifyChange);
  }

  public SubcircuitEditor get(int index)
  {
    return subcircuitEditors.get(index);
  }

  public List<SubcircuitEditor> findAll()
  {
    return subcircuitEditors;
  }

  public int size()
  {
    return subcircuitEditors.size();
  }

  public int indexOf(SubcircuitEditor subcircuitEditor)
  {
    return subcircuitEditors.indexOf(subcircuitEditor);
  }

  private SubcircuitEditor getSubcircuitEditor(SubcircuitView subcircuitView)
  {
    for (SubcircuitEditor subcircuitEditor : subcircuitEditors)
    {
      if (subcircuitEditor.getInstanceSubcircuitView() == subcircuitView)
      {
        return subcircuitEditor;
      }
    }
    throw new SimulatorException("Could not get subcircuit editor for subcircuit view [%s].", subcircuitView.getTypeName());
  }

  public String getSubcircuitNameError(String subcircuitName)
  {
    if (StringUtil.isEmptyOrNull(subcircuitName))
    {
      return "Cannot add a subcircuit type named [].";
    }

    for (SubcircuitEditor subcircuitEditor : subcircuitEditors)
    {
      if (subcircuitEditor.getTypeName().equals(subcircuitName))
      {
        return "Cannot add a subcircuit type named [%s], it is already in use.";
      }
    }
    return null;
  }

  public SubcircuitEditor getPreviousSubcircuit()
  {
    SubcircuitEditor newSubcircuitEditor = null;
    int index = subcircuitEditors.indexOf(currentSubcircuitEditor);
    if (index != -1)
    {
      index--;
      if (index < 0)
      {
        index = subcircuitEditors.size() - 1;
      }
      newSubcircuitEditor = subcircuitEditors.get(index);
    }
    return newSubcircuitEditor;
  }

  public SubcircuitEditor getNextSubcircuit()
  {
    SubcircuitEditor newSubcircuitEditor = null;
    int index = subcircuitEditors.indexOf(currentSubcircuitEditor);
    if (index != -1)
    {
      index++;
      if (index > subcircuitEditors.size() - 1)
      {
        index = 0;
      }
      newSubcircuitEditor = subcircuitEditors.get(index);
    }

    return newSubcircuitEditor;
  }

  public SubcircuitEditor getCurrentSubcircuitEditor()
  {
    return currentSubcircuitEditor;
  }

  public String setSubcircuitEditor(SubcircuitEditor subcircuitEditor, boolean notifyChange)
  {
    currentSubcircuitEditor = subcircuitEditor;
    notifySubcircuitListChanged(notifyChange);
    return currentSubcircuitEditor.getTypeName();
  }

  public void notifySubcircuitListChanged(boolean notifyChange)
  {
    if (notifyChange)
    {
      for (SubcircuitListChangedListener listener : subcircuitListChangedListeners)
      {
        listener.subcircuitListChanged();
      }
    }
  }

  @Override
  public List<SubcircuitInstanceView> getSubcircuitInstanceViews(SubcircuitView subcircuitView)
  {
    List<SubcircuitInstanceView> subcircuitInstanceViews = new ArrayList<>();
    for (SubcircuitEditor subcircuitEditor : subcircuitEditors)
    {
      SubcircuitView circuitSubcircuitView = subcircuitEditor.getInstanceSubcircuitView();
      subcircuitInstanceViews.addAll(circuitSubcircuitView.getSubcircuitInstanceViews(subcircuitView));
    }
    return subcircuitInstanceViews;
  }

  public void addSubcircuitListChangedListener(SubcircuitListChangedListener subcircuitListChangedListener)
  {
    if (!subcircuitListChangedListeners.contains(subcircuitListChangedListener))
    {
      subcircuitListChangedListeners.add(subcircuitListChangedListener);
    }
    else
    {
      throw new SimulatorException("Listener has already been added.");
    }
  }

  public void addSubcircuitListChangedListeners(List<SubcircuitListChangedListener> subcircuitListChangedListeners)
  {
    for (SubcircuitListChangedListener subcircuitListChangedListener : subcircuitListChangedListeners)
    {
      addSubcircuitListChangedListener(subcircuitListChangedListener);
    }
  }

  public List<SubcircuitListChangedListener> getSubcircuitListChangedListeners()
  {
    return subcircuitListChangedListeners;
  }
}

