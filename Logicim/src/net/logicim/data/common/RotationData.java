package net.logicim.data.common;

import net.logicim.data.PrimitiveData;
import net.logicim.ui.common.Rotation;

import java.util.Map;

public class RotationData
    extends PrimitiveData
{
  public Rotation rotation;

  public RotationData()
  {
    rotation = null;
  }

  @Override
  public void load(Map<String, String> fields)
  {
    rotation = Rotation.valueOf(fields.get("rotation"));
  }

  @Override
  public Object getObject()
  {
    return rotation;
  }
}

