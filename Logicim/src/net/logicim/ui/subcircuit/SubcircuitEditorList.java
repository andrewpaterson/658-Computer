package net.logicim.ui.subcircuit;

import net.logicim.common.SimulatorException;
import net.logicim.common.util.StringUtil;
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
  protected SubcircuitListChangedNotifier changedNotifier;

  public SubcircuitEditorList()
  {
    subcircuitEditors = new ArrayList<>();
    currentSubcircuitEditor = null;
    changedNotifier = null;
  }

  public void setChangedNotifier(SubcircuitListChangedNotifier changedNotifier)
  {
    this.changedNotifier = changedNotifier;
  }

  public void clear(boolean notifyChange)
  {
    subcircuitEditors = new ArrayList<>();
    notifyChange(notifyChange);
    currentSubcircuitEditor = null;
  }

  public void add(SubcircuitEditor subcircuitEditor, boolean notifyChange)
  {
    subcircuitEditors.add(subcircuitEditor);
    notifyChange(notifyChange);
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
      if (subcircuitEditor.getCircuitSubcircuitView() == subcircuitView)
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

  public SubcircuitEditor getSubcircuitEditor()
  {
    return currentSubcircuitEditor;
  }

  public String setSubcircuitEditor(SubcircuitEditor subcircuitEditor, boolean notifyChange)
  {
    currentSubcircuitEditor = subcircuitEditor;
    notifyChange(notifyChange);
    return currentSubcircuitEditor.getTypeName();
  }

  protected void notifyChange(boolean notifyChange)
  {
    if ((notifyChange && (changedNotifier != null)))
    {
      notifyChange();
    }
  }

  public void notifyChange()
  {
    changedNotifier.subcircuitListChanged();
  }

  public SubcircuitListChangedNotifier getChangedNotifier()
  {
    return changedNotifier;
  }

  @Override
  public List<SubcircuitInstanceView> getSubcircuitInstanceViews(SubcircuitView subcircuitView)
  {
    List<SubcircuitInstanceView> subcircuitInstanceViews = new ArrayList<>();
    for (SubcircuitEditor subcircuitEditor : subcircuitEditors)
    {
      SubcircuitView circuitSubcircuitView = subcircuitEditor.getCircuitSubcircuitView();
      subcircuitInstanceViews.addAll(circuitSubcircuitView.getSubcircuitInstanceViews(subcircuitView));
    }
    return subcircuitInstanceViews;
  }
}

