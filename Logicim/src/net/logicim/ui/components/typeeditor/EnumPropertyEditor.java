package net.logicim.ui.components.typeeditor;

import net.logicim.common.util.EnumUtil;
import net.logicim.ui.property.PropertiesPanel;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

public abstract class EnumPropertyEditor<T extends Enum<?>>
    implements PropertyEditor
{
  protected JComboBox<String> comboBox;
  protected PropertiesPanel propertiesPanel;
  protected Class<T> enumClass;
  protected Set<T> ignored;

  public EnumPropertyEditor(PropertiesPanel propertiesPanel, String name, Class<T> enumClass, T current)
  {
    this(propertiesPanel, name, enumClass, current, new HashSet<>());
  }

  public EnumPropertyEditor(PropertiesPanel propertiesPanel, String name, Class<T> enumClass, T current, Set<T> ignored)
  {
    this.propertiesPanel = propertiesPanel;
    this.enumClass = enumClass;
    this.ignored = ignored;

    comboBox = new JComboBox<>();
    comboBox.setName(name);
    List<Enum<?>> enums = EnumUtil.toList(enumClass);
    Map<Enum<?>, String> enumStringMap = EnumUtil.toEnumMap(enums);
    for (Enum<?> anEnum : enumStringMap.keySet())
    {
      if (!ignored.contains(anEnum))
      {
        String enumName = enumStringMap.get(anEnum);
        comboBox.addItem(enumName);
        if (current == anEnum)
        {
          comboBox.setSelectedItem(enumName);
        }
      }
    }
    Dimension preferredSize = comboBox.getPreferredSize();
    preferredSize.height -= 5;
    comboBox.setPreferredSize(preferredSize);
  }

  public void addIgnored(T t)
  {
    ignored.add(t);
  }

  @Override
  public T getValue()
  {
    Object selectedItem = comboBox.getSelectedItem();
    if (selectedItem != null)
    {
      return (T) EnumUtil.getEnum(enumClass, (String) selectedItem);
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
