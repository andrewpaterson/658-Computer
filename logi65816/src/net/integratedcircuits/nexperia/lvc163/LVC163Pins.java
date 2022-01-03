package net.integratedcircuits.nexperia.lvc163;

import net.common.BusValue;
import net.common.PinValue;
import net.integratedcircuits.common.counter.UpCounterCircuitPins;

public interface LVC163Pins
    extends UpCounterCircuitPins<LVC163Snapshot, LVC163Pins, LVC163>
{
  BusValue getInput();

  PinValue getPEB();

  PinValue getMRB();

  PinValue getCEP();

  PinValue getCET();
}

