package net.integratedcircuits.toshiba.vhc393;

import net.common.PinValue;
import net.common.Pins;

public interface VHC393Pins
    extends Pins<VHC393Snapshot, VHC393Pins, VHC393>
{
  PinValue getClear(int index);

  PinValue getClockB(int index);

  void setOutputUnsettled(int index);

  void setOutputError(int index);

  void setOutput(int index, long latchValue);
}

