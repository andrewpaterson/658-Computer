package net.integratedcircuits.common.counter;

import net.common.BusValue;
import net.common.IntegratedCircuit;
import net.common.PinValue;
import net.common.Pins;

public interface CounterCircuitPins<SNAPSHOT extends CounterCircuitSnapshot, PINS extends CounterCircuitPins<SNAPSHOT, PINS, ? extends CounterCircuit<SNAPSHOT, PINS>>, INTEGRATED_CIRCUIT extends IntegratedCircuit<SNAPSHOT, PINS>>
    extends Pins<SNAPSHOT, PINS, INTEGRATED_CIRCUIT>
{
  PinValue getClock();

  BusValue getInput();

  void setOutputUnsettled();

  void setOutputError();

  void setOutput(long latchValue);

  void setCarry(boolean value);
}

