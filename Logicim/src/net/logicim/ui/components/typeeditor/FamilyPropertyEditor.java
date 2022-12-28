package net.logicim.ui.components.typeeditor;

import net.logicim.domain.common.propagation.Family;
import net.logicim.domain.common.propagation.FamilyStore;

import javax.swing.*;
import java.awt.*;
import java.util.Collections;
import java.util.List;

public class FamilyPropertyEditor
    extends PropertyEditor
{
  @Override
  public Class<?> getPropertyClass()
  {
    return Family.class;
  }

  @Override
  public JComponent createEditor(String fieldName, Object fieldValue)
  {
    JComboBox<String> comboBox = new JComboBox<>();
    Family family = (Family) fieldValue;
    List<String> names = FamilyStore.getInstance().findAllNames();
    Collections.sort(names);
    for (String familyName : names)
    {
      comboBox.addItem(familyName);
      if (family.getFamily().equals(familyName))
      {
        comboBox.setSelectedItem(familyName);
      }
    }
    Dimension preferredSize = comboBox.getPreferredSize();
    preferredSize.height -= 5;
    comboBox.setPreferredSize(preferredSize);
    return comboBox;
  }
}

