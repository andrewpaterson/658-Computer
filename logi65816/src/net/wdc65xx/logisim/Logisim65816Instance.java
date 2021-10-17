package net.wdc65xx.logisim;

import com.cburch.logisim.data.BitWidth;
import com.cburch.logisim.data.Value;
import com.cburch.logisim.instance.InstanceData;
import com.cburch.logisim.instance.InstanceState;
import net.wdc65xx.wdc65816.WDC65C816;

import static net.wdc65xx.logisim.Logisim65816Factory.*;

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
    pins.setInstanceState(instanceState);

    WDC65C816 cpu = pins.getCpu();
    cpu.preTick(clock, reset);
    boolean fallingEdge = cpu.isFallingEdge();
    boolean risingEdge = cpu.isRisingEdge();

    cpu.tick();

    if (risingEdge)
    {
      instanceState.setPort(PORT_DataBus, Value.createUnknown(BitWidth.create(8)), 9);



//    instanceState.setPort(PORT_Bank, Value.createUnknown(BitWidth.create(8)), 15);
//    setPort(instanceState, PORT_MX, X, 10);

    }
  }
}

