package net.integratedcircuits.nexperia.lvc257;

import net.common.BusValue;
import net.common.PinValue;
import net.common.Pins;

public interface LVC257Pins
    extends Pins<LVC257Snapshot, LVC257Pins, LVC257>
{
  void setYError();

  void setYUnsettled();

  void setYHighImpedance();

  PinValue getSelector();

  PinValue getOEB();

  BusValue getInputValue(int port);

  void setYValue(long value);
}

