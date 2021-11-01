package net.integratedcircuits.ti.ls148;

import net.common.BusValue;
import net.common.PinValue;
import net.common.Pins;

public interface LS148Pins
    extends Pins<LS148Snapshot, LS148Pins, LS148>
{
  PinValue getEI();

  BusValue getInput();

  void setOutputError();

  void setOutputUnsettled();

  void setA(long value);

  void setGS(boolean value);

  void setEO(boolean value);
}

