package net.integratedcircuits.nexperia.lvc541;

import net.common.BusValue;
import net.common.IntegratedCircuit;
import net.common.PinValue;

public class LVC541
    extends IntegratedCircuit<LVC541Snapshot, LVC541Pins>
{
  public static final String TYPE = "4-bit Line Driver";

  public LVC541(String name, LVC541Pins pins)
  {
    super(name, pins);
  }

  public void tick()
  {
    tickPort(0);
  }

  private void tickPort(int port)
  {
    PinValue outputEnabled1B = getPins().getOE1B();
    PinValue outputEnabled2B = getPins().getOE2B();

    if (outputEnabledB.isError() || outputEnabledB.isNotConnected())
    {
      getPins().setYError(port);
    }
    else
    {
      transmit(port, !outputEnabledB.isHigh());
    }
  }

  private void transmit(int port, boolean outputEnabled)
  {
    if (outputEnabled)
    {
      BusValue aValue = getPins().getAValue();
      if (aValue.isError() || aValue.isNotConnected())
      {
        getPins().setYError(port);
      }
      else if (aValue.isUnknown())
      {
        getPins().setYUnsettled(port);
      }
      else
      {
        long value = aValue.getValue();
        getPins().setYValue(value);
      }
    }
    else
    {
      getPins().setYHighImpedance();
    }
  }

  public LVC541Snapshot createSnapshot()
  {
    return new LVC541Snapshot();
  }

  public void restoreFromSnapshot(LVC541Snapshot snapshot)
  {
  }

  @Override
  public String getType()
  {
    return TYPE;
  }
}

