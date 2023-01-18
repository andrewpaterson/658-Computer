package net.logicim.ui.simulation.component.passive.splitter;

import net.logicim.common.util.EnumUtil;
import net.logicim.domain.common.propagation.FamilyStore;
import net.logicim.ui.components.typeeditor.PropertyEditor;
import net.logicim.ui.property.PropertiesPanel;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;

public class SplitterAppearanceEditor
    implements PropertyEditor
{
  protected JComboBox<String> comboBox;
  protected PropertiesPanel propertiesPanel;

  public SplitterAppearanceEditor(PropertiesPanel propertiesPanel, String name, SplitterAppearance appearance)
  {
    this.propertiesPanel = propertiesPanel;
    comboBox = new JComboBox<>();
    comboBox.setName(name);
    List<Enum<?>> enums = EnumUtil.toList(SplitterAppearance.class);
    Map<Enum<?>, String> enumStringMap = EnumUtil.toEnumMap(enums);
    for (Enum<?> anEnum : enumStringMap.keySet())
    {
      String enumName = enumStringMap.get(anEnum);
      comboBox.addItem(enumName);
      if (appearance == anEnum)
      {
        comboBox.setSelectedItem(enumName);
      }
    }
    Dimension preferredSize = comboBox.getPreferredSize();
    preferredSize.height -= 5;
    comboBox.setPreferredSize(preferredSize);
  }

  @Override
  public Object getValue()
  {
    return FamilyStore.getInstance().get((String) comboBox.getSelectedItem());
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

