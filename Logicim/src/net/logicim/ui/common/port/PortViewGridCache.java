package net.logicim.ui.common.port;

import net.logicim.common.type.Float2D;
import net.logicim.common.type.Int2D;
import net.logicim.ui.common.Rotation;

public class PortViewGridCache
{
  protected Float2D bubbleCenter;
  protected Int2D position;
  protected boolean valid;

  public PortViewGridCache()
  {
    position = new Int2D();
    bubbleCenter = new Float2D();
    valid = false;
  }

  public void update(Float2D bubbleCenter, Int2D positionRelativeToIC, boolean inverting, Rotation icRotation, Int2D icPosition)
  {
    valid = true;
    if (inverting)
    {
      this.bubbleCenter.set(bubbleCenter);
      icRotation.transform(this.bubbleCenter);
      this.bubbleCenter.add(icPosition);
    }

    position.set(positionRelativeToIC);
    icRotation.transform(position);
    position.add(icPosition);
  }

  public Float2D getBubbleCenter()
  {
    return bubbleCenter;
  }

  public Int2D getPosition()
  {
    return position;
  }

  public void invalidate()
  {
    valid = false;
  }

  public boolean isValid()
  {
    return valid;
  }
}

