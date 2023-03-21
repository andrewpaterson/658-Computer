package net.logicim.data.field;

import net.logicim.data.common.SaveData;

import java.util.Map;

public class SaveXMLDataField
    extends XMLDataField
{
  public SaveData typeInstance;

  public SaveXMLDataField(String fieldName, SaveData typeInstance, Map<String, String> attributes)
  {
    super(fieldName, attributes);
    this.typeInstance = typeInstance;
  }
}

