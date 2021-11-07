package net.integratedcircuits.nexperia.lvc163;

import net.common.BusValue;
import net.common.PinValue;
import net.integratedcircuits.common.counter.CounterCircuitPins;

public interface LVC163Pins
    extends CounterCircuitPins<LVC163Snapshot, LVC163Pins, LVC163>
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

