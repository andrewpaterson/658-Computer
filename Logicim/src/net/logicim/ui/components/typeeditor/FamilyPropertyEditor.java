package net.logicim.ui.components.typeeditor;

import net.logicim.domain.common.propagation.Family;
import net.logicim.domain.common.propagation.FamilyStore;

import javax.swing.*;
import java.awt.*;
import java.util.Collections;
import java.util.List;

public class FamilyPropertyEditor
    implements PropertyEditor
{
  protected JComboBox<String> comboBox;

  public FamilyPropertyEditor(Family family)
  {
    comboBox = new JComboBox<>();
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
  }

  @Override
  public Object getValue()
  {
    Family family = FamilyStore.getInstance().get((String) comboBox.getSelectedItem());
    return family;
  }

  @Override
  public JComponent getComponent()
  {
    return comboBox;
  }
}
