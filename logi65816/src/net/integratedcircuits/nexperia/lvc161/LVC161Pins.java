package net.integratedcircuits.nexperia.lvc161;

import net.common.BusValue;
import net.common.PinValue;
import net.integratedcircuits.common.counter.CounterCircuitPins;

public interface LVC161Pins
    extends CounterCircuitPins<LVC161Snapshot, LVC161Pins, LVC161>
{
  BusValue getInput();

  void setOutputUnsettled();

  void setOutputError();

  void setOutput(long latchValue);

  PinValue getPEB();

  PinValue getMRB();

  PinValue getClock();

  PinValue getCEP();

  PinValue getCET();

  void setCarryUnsettled();

  void setCarryError();

  void setCarry(boolean value);
}

