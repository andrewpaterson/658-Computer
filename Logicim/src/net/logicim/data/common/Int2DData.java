package net.logicim.data.common;

import net.logicim.common.type.Int2D;

import java.util.Map;

public class Int2DData
    extends ObjectData
{
  public Int2D int2D;

  public Int2DData()
  {
    int2D = null;
  }

  @Override
  public void load(Map<String, String> fields)
  {
    int x = Integer.parseInt(fields.get("x"));
    int y = Integer.parseInt(fields.get("y"));
    int2D = new Int2D(x, y);
  }

  @Override
  public Object getObject()
  {
    return int2D;
  }
}

