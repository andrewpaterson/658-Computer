package net.integratedcircuits.nexperia.lvc126;

import net.common.IntegratedCircuit;
import net.common.PinValue;

public class LVC126
    extends IntegratedCircuit<LVC126Snapshot, LVC126Pins>
{
  public static final String TYPE = "4-bit Line Driver";

  public LVC126(String name, LVC126Pins pins)
  {
    super(name, pins);
  }

  public void tick()
  {
    tickPort(LVC126Pins.PORT_1_INDEX);
    tickPort(LVC126Pins.PORT_2_INDEX);
    tickPort(LVC126Pins.PORT_3_INDEX);
    tickPort(LVC126Pins.PORT_4_INDEX);
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
      transmit(port, outputEnabled.isHigh());
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

  public LVC126Snapshot createSnapshot()
  {
    return new LVC126Snapshot();
  }

  public void restoreFromSnapshot(LVC126Snapshot snapshot)
  {
  }

  @Override
  public String getType()
  {
    return TYPE;
  }
}

