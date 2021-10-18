package net.wdc65xx.logisim;

import com.cburch.logisim.data.Value;
import com.cburch.logisim.instance.InstanceData;
import com.cburch.logisim.instance.InstanceState;
import net.wdc65xx.wdc65816.CpuSnapshot;
import net.wdc65xx.wdc65816.WDC65C816;

import static net.wdc65xx.logisim.Logisim65816Factory.PORT_PHI2;
import static net.wdc65xx.logisim.Logisim65816Factory.PORT_RESB;

public class Logisim65816Instance
    implements InstanceData,
               Cloneable
{
  protected LogisimPins65816 pins;
  protected CpuSnapshot finalSnapshot;
  protected boolean clock;

  public Logisim65816Instance()
  {
    this.pins = new LogisimPins65816();
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

  public LogisimPins65816 getPins()
  {
    return pins;
  }

  public void tick(InstanceState instanceState)
  {
    boolean reset = instanceState.getPortValue(PORT_RESB) != Value.TRUE;
    boolean clock = instanceState.getPortValue(PORT_PHI2) != Value.FALSE;

    LogisimPins65816 pins = getPins();
    pins.setInstanceState(instanceState);
    WDC65C816 cpu = pins.getCpu();

    if ((this.clock != clock) && (finalSnapshot != null))
    {
      cpu.restoreCpuFromSnapshot(finalSnapshot);
      finalSnapshot = null;
    }

    CpuSnapshot snapshot = cpu.createCpuSnapshot();
    cpu.preTick(clock, reset);
    cpu.tick();
    finalSnapshot = cpu.createCpuSnapshot();
    cpu.restoreCpuFromSnapshot(snapshot);
  }
}

