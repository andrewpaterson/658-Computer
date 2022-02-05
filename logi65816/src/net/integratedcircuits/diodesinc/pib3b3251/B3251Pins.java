package net.integratedcircuits.diodesinc.pib3b3251;

import net.common.BusValue;
import net.common.PinValue;
import net.common.Pins;

public interface B3251Pins
    extends Pins<B3251Snapshot, B3251Pins, B3251>
{
  BusValue getInput();

  void setY(boolean value);

  BusValue getSelect();

  PinValue getEB();

  void setYUnsettled();

  void setYHighImpedance();

  void setYError();
}

