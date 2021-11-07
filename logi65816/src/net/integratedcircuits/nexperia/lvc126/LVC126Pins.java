package net.integratedcircuits.nexperia.lvc126;

import net.common.BusValue;
import net.common.PinValue;
import net.common.Pins;

public interface LVC126Pins
    extends Pins<LVC126Snapshot, LVC126Pins, LVC126>
{
  int PORT_1_INDEX = 0;
  int PORT_2_INDEX = 1;

  void setYError(int port);

  void setYUnsettled(int port);

  void setYHighImpedance(int port);

  void setYValue(int port, long value);

  PinValue getOEB(int port);

  BusValue getAValue(int port);
}

