package net.integratedcircuits.nexperia.lvc157;

import net.common.BusValue;
import net.common.PinValue;
import net.common.Pins;

public interface LVC157Pins
    extends Pins<LVC157Snapshot, LVC157Pins, LVC157>
{
  void setYError();

  void setYUnsettled();

  PinValue getSelector();

  PinValue getEB();

  BusValue getInputValue(int port);

  void setYValue(long value);
}

