package net.nexperia.lvc4245;

import net.common.BusValue;
import net.common.PinValue;

public interface LVC4245Pins
{
  int PORT_A_INDEX = 0;
  int PORT_B_INDEX = 1;

  void setTransceiver(LVC4245 transceiver);

  void setPortError(int index);

  void setPortUnsettled(int index);

  void setPortHighImpedance(int index);

  PinValue getDir();

  PinValue getOEB();

  BusValue getPortValue(int index);

  void setPortValue(int index, long value);
}

