package net.logicim.data.common;

import net.logicim.common.type.Int2D;
import net.logicim.data.SaveData;

public class Int2DData
    extends SaveData
{
  public int x;
  public int y;

  public Int2DData(Int2D p)
  {
    this.x = p.x;
    this.y = p.y;
  }

  public Int2D toInt2D()
  {
    return new Int2D(x, y);
  }
}

