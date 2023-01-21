package net.logicim.ui.components.typeeditor;

import net.logicim.ui.property.PropertiesPanel;

import java.util.Set;

public class GeneralEnumPropertyEditor
    extends EnumPropertyEditor<Enum<?>>
{
  public GeneralEnumPropertyEditor(PropertiesPanel propertiesPanel, String name, Class<Enum<?>> enumClass, Enum<?> current)
  {
    super(propertiesPanel, name, enumClass, current);
  }

  public GeneralEnumPropertyEditor(PropertiesPanel propertiesPanel, String name, Class<Enum<?>> enumClass, Enum<?> current, Set<Enum<?>> ignored)
  {
    super(propertiesPanel, name, enumClass, current, ignored);
  }
}

