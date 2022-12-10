package net.logicim.data.field;

import java.util.Map;

public class XMLDataField
{
  public String fieldName;
  public Map<String, String> attributes;

  public XMLDataField(String fieldName, Map<String, String> attributes)
  {
    this.fieldName = fieldName;
    this.attributes = attributes;
  }
}

