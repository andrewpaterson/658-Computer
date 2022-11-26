package net.logicim.data.field;

import net.logicim.data.SaveData;

public class TypedDataField
    extends DataField
{
  public SaveData typeInstance;

  public TypedDataField(String fieldName, SaveData typeInstance)
  {
    super(fieldName);
    this.typeInstance = typeInstance;
  }
}

