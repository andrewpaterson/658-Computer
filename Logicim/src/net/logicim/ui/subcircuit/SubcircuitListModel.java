package net.logicim.ui.subcircuit;

import net.logicim.ui.simulation.subcircuit.SubcircuitEditor;

import javax.swing.*;

public class SubcircuitListModel
    extends AbstractListModel<SubcircuitEditor>
    implements SubcircuitListChangedNotifier
{
  protected SubcircuitList subcircuitList;
  protected JList<SubcircuitEditor> list;

  public SubcircuitListModel(SubcircuitList subcircuitList, JList<SubcircuitEditor> list)
  {
    this.subcircuitList = subcircuitList;
    this.list = list;
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

  @Override
  public void subcircuitListChanged()
  {
    list.updateUI();
  }
}

