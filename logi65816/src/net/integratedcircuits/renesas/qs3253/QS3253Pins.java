package net.integratedcircuits.renesas.qs3253;

import net.common.BusValue;
import net.common.PinValue;
import net.common.Pins;

public interface QS3253Pins
    extends Pins<QS3253Snapshot, QS3253Pins, QS3253>
{
  BusValue getInputA();

  BusValue getInputB();

  void setYA(boolean value);

  void setYB(boolean value);

  BusValue getSelect();

  PinValue getEAB();

  PinValue getEBB();

  void setYAUnsettled();

  void setYBUnsettled();

  void setYAHighImpedance();

  void setYBHighImpedance();

  void setYAError();

  void setYBError();
}

