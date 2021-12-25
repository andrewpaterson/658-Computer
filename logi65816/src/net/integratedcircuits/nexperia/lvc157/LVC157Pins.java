package net.integratedcircuits.nexperia.lvc157;

import net.common.BusValue;
import net.common.PinValue;
import net.common.Pins;

public interface LVC157Pins
    extends Pins<LVC157Snapshot, LVC157Pins, LVC157>
{
  void setYError();

  void setYUnsettled();

  void setYHighImpedance();

  PinValue getSelector();

  PinValue getOEB();

  BusValue getInputValue(int port);

  void setYValue(long value);
}

