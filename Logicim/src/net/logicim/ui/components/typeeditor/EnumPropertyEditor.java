package net.logicim.ui.components.typeeditor;

import net.logicim.common.util.EnumUtil;
import net.logicim.ui.property.PropertiesPanel;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;

public abstract class EnumPropertyEditor<T extends Enum<?>>
    implements PropertyEditor
{
  protected JComboBox<String> comboBox;
  protected PropertiesPanel propertiesPanel;
  protected Class<T> enumClass;

  public EnumPropertyEditor(PropertiesPanel propertiesPanel, String name, Class<T> enumClass, T current)
  {
    this.propertiesPanel = propertiesPanel;
    this.enumClass = enumClass;

    comboBox = new JComboBox<>();
    comboBox.setName(name);
    List<Enum<?>> enums = EnumUtil.toList(enumClass);
    Map<Enum<?>, String> enumStringMap = EnumUtil.toEnumMap(enums);
    for (Enum<?> anEnum : enumStringMap.keySet())
    {
      String enumName = enumStringMap.get(anEnum);
      comboBox.addItem(enumName);
      if (current == anEnum)
      {
        comboBox.setSelectedItem(enumName);
      }
    }
    Dimension preferredSize = comboBox.getPreferredSize();
    preferredSize.height -= 5;
    comboBox.setPreferredSize(preferredSize);

  }

  @Override
  public T getValue()
  {
    Object selectedItem = comboBox.getSelectedItem();
    if (selectedItem != null)
    {
      return (T) EnumUtil.getEnum(enumClass, (String)selectedItem);
    }
    else
    {
      return null;
    }
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

