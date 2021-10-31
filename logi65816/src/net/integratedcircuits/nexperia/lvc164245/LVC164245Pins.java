package net.integratedcircuits.nexperia.lvc164245;

import net.common.BusValue;
import net.common.PinValue;
import net.common.Pins;

public interface LVC164245Pins
    extends Pins<LVC164245Snapshot, LVC164245Pins, LVC164245>
{
  int PORT_A_INDEX = 0;
  int PORT_B_INDEX = 1;

  void setPortError(int port, int index);

  void setPortUnsettled(int port, int index);

  void setPortHighImpedance(int port, int index);

  PinValue getDir(int port);

  PinValue getOEB(int port);

  BusValue getPortValue(int port, int index);

  void setPortValue(int port, int index, long value);
}

