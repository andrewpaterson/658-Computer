package net.integratedcircuits.nexperia.lvc541;

import net.common.BusValue;
import net.common.PinValue;
import net.common.Pins;

public interface LVC541Pins
    extends Pins<LVC541Snapshot, LVC541Pins, LVC541>
{
  void setYError();

  void setYUnsettled();

  void setYHighImpedance();

  void setYValue(long value);

  PinValue getOE1B();

  PinValue getOE2B();

  BusValue getAValue();
}

