package net.logicim.domain.integratedcircuit.nexperia;

import net.logicim.domain.common.*;

public class LVC541Pins
    extends TickablePins
{
  protected Omniport y;
  protected Omniport a;
  protected Uniport oe1B;
  protected Uniport oe2B;

  public LVC541Pins(Tickables tickables)
  {
    super(tickables);
  }

  void setYError()
  {
    y.error();
  }

  void setYUnsettled()
  {
    y.unset();
  }

  void setYHighImpedance()
  {
    y.highImpedance();
  }

  void setYValue(long value)
  {
    y.writeAllPinsBool(value);
  }

  PinValue getOE1B()
  {
    return getPinValue(oe1B);
  }

  PinValue getOE2B()
  {
    return getPinValue(oe2B);
  }

  BusValue getAValue()
  {
    return getBusValue(a);
  }
}

