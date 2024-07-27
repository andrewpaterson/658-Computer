package net.logicim.ui.components.table;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.EventObject;

import static java.awt.Font.PLAIN;

public class TableCell
    implements TableCellRenderer,
               TableCellEditor
{
  @Override
  public Component getTableCellRendererComponent(JTable table, Object cellValue, boolean isSelected, boolean hasFocus, int row, int column)
  {
    TableValue tableValue = (TableValue) cellValue;

    Class<?> valueClass = tableValue.getValueClass();
    if (valueClass.equals(String.class))
    {
      JLabel label = new JLabel();
      label.setFont(label.getFont().deriveFont(PLAIN));
      label.setText((String) tableValue.value);
      return label;
    }
    else
    {
      JPanel jPanel = new JPanel();
      jPanel.setOpaque(true);
      return jPanel;
    }
  }

  @Override
  public Component getTableCellEditorComponent(JTable table, Object cellValue, boolean isSelected, int row, int column)
  {
    TableValue tableValue = (TableValue) cellValue;

    Class<?> valueClass = tableValue.getValueClass();
    if (valueClass.equals(String.class))
    {
      JTextField textField = new JTextField();
      textField.setFont(textField.getFont().deriveFont(PLAIN));
      textField.setText((String) tableValue.value);
      return textField;
    }
    else
    {
      JPanel jPanel = new JPanel();
      jPanel.setOpaque(true);
      return jPanel;
    }
  }

  @Override
  public Object getCellEditorValue()
  {
    return null;
  }

  @Override
  public boolean isCellEditable(EventObject anEvent)
  {
    return true;
  }

  @Override
  public boolean shouldSelectCell(EventObject anEvent)
  {
    return true;
  }

  @Override
  public boolean stopCellEditing()
  {
    return true;
  }

  @Override
  public void cancelCellEditing()
  {
  }

  @Override
  public void addCellEditorListener(CellEditorListener l)
  {
  }

  @Override
  public void removeCellEditorListener(CellEditorListener l)
  {
  }
}

