package net.integratedcircuits.nexperia.lvc74;

import net.common.PinValue;
import net.common.Pins;

public interface LVC74Pins
    extends Pins<LVC74Snapshot, LVC74Pins, LVC74>
{
  int FLIP_FLOP_1_INDEX = 0;
  int FLIP_FLOP_2_INDEX = 1;

  PinValue getCPValue(int flipFlop);

  PinValue getRDValue(int flipFlop);

  PinValue getDValue(int flipFlop);

  PinValue getPrevDValue(int flipFlop);

  PinValue getSDValue(int flipFlop);

  void setValue(int flipFlop, boolean value);

  long getTickCount();
}

