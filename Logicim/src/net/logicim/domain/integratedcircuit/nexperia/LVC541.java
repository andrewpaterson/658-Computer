package net.logicim.domain.integratedcircuit.nexperia;

import net.logicim.domain.common.BusValue;
import net.logicim.domain.common.IntegratedCircuit;
import net.logicim.domain.common.PinValue;

public class LVC541
    extends IntegratedCircuit<LVC541Pins>
{
  public static final String TYPE = "8-bit Line Driver";

  public LVC541(String name, LVC541Pins pins)
  {
    super(name, pins);
  }

  public void tick()
  {
    tickPort();
  }

  private void tickPort()
  {
//    PinValue outputEnabled1B = getPins().getOE1B();
//    PinValue outputEnabled2B = getPins().getOE2B();
//
//    if (outputEnabled1B.isError() || outputEnabled1B.isNotConnected() ||
//        outputEnabled2B.isError() || outputEnabled2B.isNotConnected())
//    {
//      getPins().setYError();
//    }
//    else
//    {
//      transmit(!outputEnabled1B.isHigh() && !outputEnabled2B.isHigh());
//    }
  }

  private void transmit(boolean outputEnabled)
  {
//    if (outputEnabled)
//    {
//      BusValue aValue = getPins().getAValue();
//      if (aValue.isError() || aValue.isNotConnected())
//      {
//        getPins().setYError();
//      }
//      else if (aValue.isUnknown())
//      {
//        getPins().setYUnsettled();
//      }
//      else
//      {
//        long value = aValue.getValue();
//        getPins().setYValue(value);
//      }
//    }
//    else
//    {
//      getPins().setYHighImpedance();
//    }
  }

  @Override
  public String getType()
  {
    return TYPE;
  }
}

