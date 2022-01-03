package net.integratedcircuits.common.counter;

import net.common.IntegratedCircuit;
import net.common.PinValue;
import net.common.Pins;

public interface UpCounterCircuitPins<SNAPSHOT extends UpCounterCircuitSnapshot, PINS extends UpCounterCircuitPins<SNAPSHOT, PINS, ? extends UpCounterCircuit<SNAPSHOT, PINS>>, INTEGRATED_CIRCUIT extends IntegratedCircuit<SNAPSHOT, PINS>>
    extends Pins<SNAPSHOT, PINS, INTEGRATED_CIRCUIT>
{
  PinValue getClock();

  void setOutputUnsettled();

  void setOutputError();

  void setOutput(long latchValue);

  void setCarry(boolean value);
}

