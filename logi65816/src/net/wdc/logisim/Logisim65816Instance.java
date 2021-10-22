package net.wdc.logisim;

import com.cburch.logisim.data.Value;
import com.cburch.logisim.instance.InstanceData;
import com.cburch.logisim.instance.InstanceState;
import net.wdc.wdc65816.CpuSnapshot;
import net.wdc.wdc65816.WDC65C816;

import static net.wdc.logisim.Logisim65816Factory.PORT_PHI2;

public class Logisim65816Instance
    implements InstanceData,
               Cloneable
{
  protected WDC65C816LogisimPins pins;
  protected CpuSnapshot finalSnapshot;
  protected boolean clock;

  public Logisim65816Instance()
  {
    this.pins = new WDC65C816LogisimPins();
    new WDC65C816(pins);
    finalSnapshot = null;
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

  public WDC65C816LogisimPins getPins()
  {
    return pins;
  }

  public void tick(InstanceState instanceState)
  {
    boolean clock = instanceState.getPortValue(PORT_PHI2) == Value.TRUE;

    WDC65C816LogisimPins pins = getPins();
    pins.setInstanceState(instanceState);
    WDC65C816 cpu = pins.getCpu();

    if ((this.clock != clock) && (finalSnapshot != null))
    {
      cpu.restoreCpuFromSnapshot(finalSnapshot);
      finalSnapshot = null;
      this.clock = clock;
    }

    CpuSnapshot snapshot = cpu.createCpuSnapshot();
    cpu.preTick(clock);
    cpu.tick();
    finalSnapshot = cpu.createCpuSnapshot();
    cpu.restoreCpuFromSnapshot(snapshot);
  }
}

