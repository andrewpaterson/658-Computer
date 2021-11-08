package net.integratedcircuits.nexperia.lvc125;

import net.common.IntegratedCircuit;
import net.common.PinValue;

public class LVC125
    extends IntegratedCircuit<LVC125Snapshot, LVC125Pins>
{
  public static final String TYPE = "4-bit Line Driver";

  public LVC125(String name, LVC125Pins pins)
  {
    super(name, pins);
  }

  public void tick()
  {
    tickPort(LVC125Pins.PORT_1_INDEX);
    tickPort(LVC125Pins.PORT_2_INDEX);
    tickPort(LVC125Pins.PORT_3_INDEX);
    tickPort(LVC125Pins.PORT_4_INDEX);
  }

  private void tickPort(int port)
  {
    PinValue outputEnabled = getPins().getOE(port);

    if (outputEnabled.isError() || outputEnabled.isNotConnected())
    {
      getPins().setYError(port);
    }
    else
    {
      transmit(port, outputEnabled.isLow());
    }
  }

  private void transmit(int port, boolean outputEnabled)
  {
    if (outputEnabled)
    {
      PinValue aValue = getPins().getAValue(port);
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
        getPins().setYValue(port, aValue.isHigh());
      }
    }
    else
    {
      getPins().setYHighImpedance(port);
    }
  }

  public LVC125Snapshot createSnapshot()
  {
    return new LVC125Snapshot();
  }

  public void restoreFromSnapshot(LVC125Snapshot snapshot)
  {
  }

  @Override
  public String getType()
  {
    return TYPE;
  }
}

