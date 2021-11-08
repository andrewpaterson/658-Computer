package net.integratedcircuits.nexperia.lvc125;

import net.common.PinValue;
import net.common.Pins;

public interface LVC125Pins
    extends Pins<LVC125Snapshot, LVC125Pins, LVC125>
{
  int PORT_1_INDEX = 0;
  int PORT_2_INDEX = 1;
  int PORT_3_INDEX = 2;
  int PORT_4_INDEX = 3;

  void setYError(int port);

  void setYUnsettled(int port);

  void setYHighImpedance(int port);

  void setYValue(int port, boolean value);

  PinValue getOE(int port);

  PinValue getAValue(int port);
}

