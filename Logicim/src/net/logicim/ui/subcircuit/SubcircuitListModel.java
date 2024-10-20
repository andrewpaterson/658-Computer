package net.logicim.ui.subcircuit;

import net.logicim.ui.simulation.subcircuit.SubcircuitEditor;

import javax.swing.*;

public class SubcircuitListModel
    extends AbstractListModel<SubcircuitEditor>
{
  protected SubcircuitEditorListHolder subcircuitEditorListHolder;

  public SubcircuitListModel(SubcircuitEditorListHolder subcircuitEditorListHolder)
  {
    this.subcircuitEditorListHolder = subcircuitEditorListHolder;
  }

  @Override
  public int getSize()
  {
    return subcircuitEditorListHolder.getSubcircuitEditorList().size();
  }

  @Override
  public SubcircuitEditor getElementAt(int index)
  {
    return subcircuitEditorListHolder.getSubcircuitEditorList().get(index);
  }

  @Override
  protected void fireContentsChanged(Object source, int index0, int index1)
  {
    super.fireContentsChanged(source, index0, index1);
  }

  public int getSubcircuitEditorIndex()
  {
    SubcircuitEditorList subcircuitEditorList = subcircuitEditorListHolder.getSubcircuitEditorList();
    SubcircuitEditor subcircuitEditor = subcircuitEditorList.getCurrentSubcircuitEditor();
    return subcircuitEditorList.indexOf(subcircuitEditor);
  }
}

