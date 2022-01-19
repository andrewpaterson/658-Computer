package net.integratedcircuits.toshiba.vhc161;

import net.common.BusValue;
import net.common.PinValue;
import net.integratedcircuits.common.counter.UpCounterCircuitPins;

public interface VHC161Pins
    extends UpCounterCircuitPins<VHC161Snapshot, VHC161Pins, VHC161>
{
  BusValue getInput();

  PinValue getParallelLoadB();

  PinValue getMasterResetB();

  PinValue getCountEnabled();

  PinValue getCET();
}

