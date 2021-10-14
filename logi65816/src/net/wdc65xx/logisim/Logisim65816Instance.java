package net.wdc65xx.logisim;

import com.cburch.logisim.instance.InstanceData;
import com.cburch.logisim.instance.InstanceState;
import net.wdc65xx.wdc65816.Cpu65816;

public class Logisim65816Instance
    implements InstanceData,
               Cloneable
{
  protected Cpu65816 cpu;
  protected LogisimPins65816 pins;

  public Logisim65816Instance()
  {
    this.pins = new LogisimPins65816();
    this.cpu = new Cpu65816(pins);
  }

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
  public void setInstanceState(InstanceState instanceState)
  {
    pins.setInstanceState(instanceState);
  }

  public Cpu65816 getCpu()
  {
    return cpu;
  }
}

