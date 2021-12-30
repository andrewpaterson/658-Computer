package net.integratedcircuits.toshiba.vhc238;

import net.common.BusValue;
import net.common.PinValue;
import net.common.Pins;

public interface VHC238Pins
    extends Pins<VHC238Snapshot, VHC238Pins, VHC238>
{
  PinValue getE1B();

  PinValue getE2B();

  PinValue getE3();

  BusValue getA();

  void setOutputError();

  void setOutputUnsettled();

  void setOutput(long outputValue);
}

