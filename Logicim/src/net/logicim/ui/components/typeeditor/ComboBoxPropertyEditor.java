package net.logicim.ui.components.typeeditor;

import net.logicim.ui.property.PropertiesPanel;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class ComboBoxPropertyEditor
    implements PropertyEditor
{
  protected JComboBox<String> comboBox;
  protected PropertiesPanel propertiesPanel;

  public ComboBoxPropertyEditor(PropertiesPanel propertiesPanel, String name, List<String> items, String current)
  {
    this.propertiesPanel = propertiesPanel;

    comboBox = new JComboBox<>();
    comboBox.setName(name);
    for (String item : items)
    {
      comboBox.addItem(item);
      if (item.equals(current))
      {
        comboBox.setSelectedItem(item);
      }
    }
    Dimension preferredSize = comboBox.getPreferredSize();
    preferredSize.height -= 5;
    comboBox.setPreferredSize(preferredSize);
  }

  @Override
  public String getValue()
  {
    return (String) comboBox.getSelectedItem();
  }

  @Override
  public JComponent getComponent()
  {
    return comboBox;
  }

  @Override
  public PropertiesPanel getPropertiesPanel()
  {
    return propertiesPanel;
  }
}

