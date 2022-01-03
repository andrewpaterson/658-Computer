package net.integratedcircuits.nexperia.lvc161;

import net.common.BusValue;
import net.common.PinValue;
import net.integratedcircuits.common.counter.UpCounterCircuitPins;

public interface LVC161Pins
    extends UpCounterCircuitPins<LVC161Snapshot, LVC161Pins, LVC161>
{
  BusValue getInput();

  PinValue getParallelLoadB();

  PinValue getMasterResetB();

  PinValue getCountEnabled();

  PinValue getCET();
}

