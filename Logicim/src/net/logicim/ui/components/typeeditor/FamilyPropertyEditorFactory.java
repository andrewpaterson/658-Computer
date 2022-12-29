package net.logicim.ui.components.typeeditor;

import net.logicim.domain.common.propagation.Family;

public class FamilyPropertyEditorFactory
    extends PropertyEditorFactory
{
  @Override
  public Class<?> getPropertyClass()
  {
    return Family.class;
  }

  @Override
  public PropertyEditor createEditor(String fieldName, Object fieldValue)
  {
    return new FamilyPropertyEditor((Family) fieldValue);
  }
}

