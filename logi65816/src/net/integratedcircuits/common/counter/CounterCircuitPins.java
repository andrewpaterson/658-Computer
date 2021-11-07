package net.integratedcircuits.common.counter;

import net.common.BusValue;
import net.common.IntegratedCircuit;
import net.common.Pins;

public interface CounterCircuitPins<SNAPSHOT extends CounterCircuitSnapshot, PINS extends CounterCircuitPins<SNAPSHOT, PINS, ? extends CounterCircuit<SNAPSHOT, PINS>>, INTEGRATED_CIRCUIT extends IntegratedCircuit<SNAPSHOT, PINS>>
    extends Pins<SNAPSHOT, PINS, INTEGRATED_CIRCUIT>
{
  void setOutput(long value);

  void setCarry(boolean carry);

  BusValue getInput();
}

