package net.wdc65xx.logisim;

import com.cburch.logisim.data.Value;
import com.cburch.logisim.instance.InstanceData;
import com.cburch.logisim.instance.InstanceState;
import net.wdc65xx.wdc65816.WDC65C816;

import static net.wdc65xx.logisim.Logisim65816Factory.PORT_PHI2;
import static net.wdc65xx.logisim.Logisim65816Factory.PORT_RESB;

public class Logisim65816Instance
    implements InstanceData,
               Cloneable
{
  protected LogisimPins65816 pins;

  public Logisim65816Instance()
  {
    this.pins = new LogisimPins65816();
    new WDC65C816(pins);
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

    WDC65C816 cpu = pins.getCpu();
    cpu.preTick(clock, reset);
    boolean fallingEdge = cpu.isFallingEdge();
    boolean risingEdge = cpu.isRisingEdge();

    if (fallingEdge)
    {
      pins.readInputs(instanceState);
    }

    cpu.tick();

    if (risingEdge)
    {
      pins.writeOutputs(instanceState);
    }
  }
}

