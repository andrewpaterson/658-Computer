package net.logicim.ui.subcircuit;

import net.logicim.common.SimulatorException;
import net.logicim.common.util.StringUtil;
import net.logicim.ui.circuit.SubcircuitView;
import net.logicim.ui.simulation.subcircuit.SubcircuitEditor;

import java.util.ArrayList;
import java.util.List;

public class SubcircuitList
{
  protected List<SubcircuitEditor> subcircuitEditors;
  protected SubcircuitListChangedNotifier changedNotifier;

  public SubcircuitList()
  {
    subcircuitEditors = new ArrayList<>();
    changedNotifier = null;
  }

  public void setChangedNotifier(SubcircuitListChangedNotifier changedNotifier)
  {
    this.changedNotifier = changedNotifier;
  }

  public void clear(boolean notifyChange)
  {
    subcircuitEditors = new ArrayList<>();
    if ((notifyChange && (changedNotifier != null)))
    {
      changedNotifier.subcircuitListChanged();
    }
  }

  public void add(SubcircuitEditor subcircuitEditor, boolean notifyChange)
  {
    subcircuitEditors.add(subcircuitEditor);
    if ((notifyChange && (changedNotifier != null)))
    {
      changedNotifier.subcircuitListChanged();
    }
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

  public void notifyChange()
  {
    changedNotifier.subcircuitListChanged();
  }
}

