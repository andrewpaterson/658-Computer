package net.logicim.ui.common.table;

import javax.swing.*;

public class Table
    extends JTable
{
  public Table()
  {
    super(new TableModel());
    setDefaultRenderer(Object.class, new TableCell());
    setDefaultEditor(Object.class, new TableCell());
    getTableHeader().setReorderingAllowed(false);
    putClientProperty("terminateEditOnFocusLost", Boolean.TRUE);
  }

  @Override
  public TableModel getModel()
  {
    return (TableModel) super.getModel();
  }
}

