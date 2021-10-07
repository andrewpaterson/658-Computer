package name.bizna.logisim;

import com.cburch.logisim.instance.InstanceData;
import com.cburch.logisim.instance.InstanceState;
import name.bizna.cpu.Cpu65816;

public class Logisim65816Data
    implements InstanceData,
               Cloneable
{
  protected Cpu65816 cpu;
  protected Logisim65816Factory parent;

  public Logisim65816Data(Logisim65816Factory parent, InstanceState state)
  {
    this.parent = parent;
    this.cpu = new Cpu65816(new LogisimPins65816(state));
  }

  public static Logisim65816Data getOrCreateLogisim65816Data(InstanceState state, Logisim65816Factory factory)
  {
    Logisim65816Data ret = (Logisim65816Data) state.getData();
    if (ret == null)
    {
      ret = new Logisim65816Data(factory, state);
      state.setData(ret);
    }
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

  protected boolean isOpcodeValid()
  {
    return cpu.getCycle() != 0;
  }

  public void propagate()
  {
    cpu.tick();
  }

  public Cpu65816 getCpu()
  {
    return cpu;
  }

}

