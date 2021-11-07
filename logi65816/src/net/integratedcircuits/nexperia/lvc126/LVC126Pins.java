package net.integratedcircuits.nexperia.lvc16244;

import net.common.BusValue;
import net.common.PinValue;
import net.common.Pins;

public interface LVC16244Pins
    extends Pins<LVC16244Snapshot, LVC16244Pins, LVC16244>
{
  int PORT_1_INDEX = 0;
  int PORT_2_INDEX = 1;
  int PORT_3_INDEX = 2;
  int PORT_4_INDEX = 3;

  void setYError(int port);

  void setYUnsettled(int port);

  void setYHighImpedance(int port);

  void setYValue(int port, long value);

  PinValue getOEB(int port);

  BusValue getAValue(int port);
}

