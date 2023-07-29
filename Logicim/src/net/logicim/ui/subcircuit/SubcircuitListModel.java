package net.logicim.ui.subcircuit;

import net.logicim.ui.simulation.subcircuit.SubcircuitEditor;

import javax.swing.*;

public class SubcircuitListModel
    extends AbstractListModel<SubcircuitEditor>
{
  protected SubcircuitList subcircuitList;

  public SubcircuitListModel(SubcircuitList subcircuitList)
  {
    this.subcircuitList = subcircuitList;
  }

  @Override
  public int getSize()
  {
    return subcircuitList.size();
  }

  @Override
  public SubcircuitEditor getElementAt(int index)
  {
    return subcircuitList.get(index);
  }

  @Override
  protected void fireContentsChanged(Object source, int index0, int index1)
  {
    super.fireContentsChanged(source, index0, index1);
  }

  public int getSubcircuitEditorIndex()
  {
    SubcircuitEditor subcircuitEditor = subcircuitList.getSubcircuitEditor();
    return subcircuitList.indexOf(subcircuitEditor);
  }
}

