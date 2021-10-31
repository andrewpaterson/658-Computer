package net.logisim.integratedcircuits.nexperia.lvc4245;

import com.cburch.logisim.data.BitWidth;
import com.cburch.logisim.data.Value;
import net.common.BusValue;
import net.common.PinValue;
import net.integratedcircuits.nexperia.lvc4245.LVC4245;
import net.integratedcircuits.nexperia.lvc4245.LVC4245Pins;
import net.integratedcircuits.nexperia.lvc4245.LVC4245Snapshot;
import net.logisim.common.LogisimPins;

public class LVC4245LogisimPins
    extends LogisimPins<LVC4245Snapshot, LVC4245Pins, LVC4245>
    implements LVC4245Pins
{
  protected int[] ports;

  public LVC4245LogisimPins()
  {
    this.ports = new int[2];
    ports[PORT_A_INDEX] = LVC4245Factory.PORT_A;
    ports[PORT_B_INDEX] = LVC4245Factory.PORT_B;
  }

  @Override
  public void setPortError(int index)
  {
    instanceState.setPort(ports[index], Value.createError(BitWidth.create(8)), 2);
  }

  @Override
  public void setPortUnsettled(int index)
  {
  }

  @Override
  public void setPortHighImpedance(int index)
  {
    instanceState.setPort(ports[index], Value.createUnknown(BitWidth.create(8)), 2);
  }

  @Override
  public PinValue getDir()
  {
    PinValue pinValue = getPinValue(LVC4245Factory.PORT_DIR);
    PinValue direction = getIntegratedCircuit().getDirection();
    if (direction != pinValue)
    {
      if (pinValue.isLow())
      {
        instanceState.setPort(ports[PORT_B_INDEX], Value.createUnknown(BitWidth.create(8)), 4);
      }
      else if (pinValue.isHigh())
      {
        instanceState.setPort(ports[PORT_A_INDEX], Value.createUnknown(BitWidth.create(8)), 4);
      }
    }

    return pinValue;
  }

  @Override
  public PinValue getOEB()
  {
    return getPinValue(LVC4245Factory.PORT_OEB);
  }

  @Override
  public BusValue getPortValue(int index)
  {
    return getBusValue(ports[index], 8, 2);
  }

  @Override
  public void setPortValue(int index, long value)
  {
    instanceState.setPort(ports[index], Value.createKnown(BitWidth.create(8), value), 2);
  }
}

