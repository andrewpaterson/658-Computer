package net.logicim.ui.subcircuit;

import net.logicim.ui.simulation.subcircuit.SubcircuitEditor;

import javax.swing.*;

public class SubcircuitListModel
    extends AbstractListModel<SubcircuitEditor>
{
  protected SubcircuitEditorList subcircuitEditorList;

  public SubcircuitListModel(SubcircuitEditorList subcircuitEditorList)
  {
    this.subcircuitEditorList = subcircuitEditorList;
  }

  @Override
  public int getSize()
  {
    return subcircuitEditorList.size();
  }

  @Override
  public SubcircuitEditor getElementAt(int index)
  {
    return subcircuitEditorList.get(index);
  }

  @Override
  protected void fireContentsChanged(Object source, int index0, int index1)
  {
    super.fireContentsChanged(source, index0, index1);
  }

  public int getSubcircuitEditorIndex()
  {
    SubcircuitEditor subcircuitEditor = subcircuitEditorList.getCurrentSubcircuitEditor();
    return subcircuitEditorList.indexOf(subcircuitEditor);
  }
}

