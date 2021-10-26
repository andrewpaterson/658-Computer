package net.nexperia.lvc4245;

import net.common.BusValue;
import net.common.PinValue;
import net.common.Pins;

public interface LVC4245Pins
    extends Pins<LVC4245Snapshot, LVC4245Pins, LVC4245>
{
  int PORT_A_INDEX = 0;
  int PORT_B_INDEX = 1;

  void setPortError(int index);

  void setPortUnsettled(int index);

  void setPortHighImpedance(int index);

  PinValue getDir();

  PinValue getOEB();

  BusValue getPortValue(int index);

  void setPortValue(int index, long value);
}

