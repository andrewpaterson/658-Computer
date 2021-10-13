package net.wdc65xx.logisim;

import com.cburch.logisim.instance.InstanceData;
import com.cburch.logisim.instance.InstanceState;
import net.wdc65xx.wdc65816.Cpu65816;

public class Logisim65816Data
    implements InstanceData,
               Cloneable
{
  protected Cpu65816 cpu;
  protected Logisim65816Factory parent;
  protected LogisimPins65816 pins;

  public Logisim65816Data(Logisim65816Factory parent)
  {
    this.parent = parent;

    this.pins = new LogisimPins65816();
    this.cpu = new Cpu65816(pins);
  }

  public static Logisim65816Data getOrCreateLogisim65816Data(InstanceState state, Logisim65816Factory factory)
  {
    Logisim65816Data ret = (Logisim65816Data) state.getData();
    if (ret == null)
    {
      ret = new Logisim65816Data(factory);
      state.setData(ret);
    }
    ret.setInstanceState(state);
    return ret;
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

  protected boolean isOpcodeValid()
  {
    return cpu.getCycle() != 0;
  }

  public void tick()
  {
    cpu.tick();
  }

  public Cpu65816 getCpu()
  {
    return cpu;
  }

}

