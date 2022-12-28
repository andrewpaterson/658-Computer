package net.logicim.ui.components.table;

import javax.swing.table.DefaultTableModel;

public class TableModel
    extends DefaultTableModel
{
  @Override
  public boolean isCellEditable(int row, int column)
  {
    if (column == 0)
    {
      return false;
    }
    else
    {
      return super.isCellEditable(row, column);
    }
  }
}
