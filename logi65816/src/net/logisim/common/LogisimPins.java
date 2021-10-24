package net.logisim.common;

import com.cburch.logisim.instance.InstanceData;
import com.cburch.logisim.instance.InstanceState;

public abstract class LogisimPins
    implements InstanceData
{
  protected InstanceState instanceState;

  @Override
  public Object clone()
  {
    try
    {
      return super.clone();
    }
    catch (CloneNotSupportedException e)
    {
      return null;
    }
  }

  public void donePropagation()
  {
  }

  public void startPropagation()
  {
  }

  public void setInstanceState(InstanceState instanceState)
  {
    this.instanceState = instanceState;
  }

  public boolean isClockHigh()
  {
    return false;
  }

  public abstract void tick();
}

