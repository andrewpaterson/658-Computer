package net.integratedcircuits.nexperia.lvc16244;

import net.common.BusValue;
import net.common.IntegratedCircuit;
import net.common.PinValue;

public class LVC16244
    extends IntegratedCircuit<LVC16244Snapshot, LVC16244Pins>
{
  public static final String TYPE = "Line Driver";

  public LVC16244(String name, LVC16244Pins pins)
  {
    super(name, pins);
  }

  public void tick()
  {
    tickPort(LVC16244Pins.PORT_1_INDEX);
    tickPort(LVC16244Pins.PORT_2_INDEX);
    tickPort(LVC16244Pins.PORT_3_INDEX);
    tickPort(LVC16244Pins.PORT_4_INDEX);
  }

  private void tickPort(int port)
  {
    PinValue outputEnabledB = getPins().getOEB(port);

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
      BusValue aValue = getPins().getAValue(port);
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
        getPins().setYValue(port, value);
      }
    }
    else
    {
      getPins().setYHighImpedance(port);
    }
  }

  public LVC16244Snapshot createSnapshot()
  {
    return new LVC16244Snapshot();
  }

  public void restoreFromSnapshot(LVC16244Snapshot snapshot)
  {
  }

  @Override
  public String getType()
  {
    return TYPE;
  }
}

