package net.logicim.ui.components.typeeditor;

import java.util.Set;

public class GeneralEnumPropertyEditor
    extends EnumPropertyEditor<Enum<?>>
{
  public GeneralEnumPropertyEditor(String name, Class<Enum<?>> enumClass, Enum<?> current)
  {
    super(name, enumClass, current);
  }

  public GeneralEnumPropertyEditor(String name, Class<Enum<?>> enumClass, Enum<?> current, Set<Enum<?>> ignored)
  {
    super(name, enumClass, current, ignored);
  }
}

