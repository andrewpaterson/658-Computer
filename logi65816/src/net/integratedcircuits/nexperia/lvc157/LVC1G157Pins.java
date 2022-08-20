package net.integratedcircuits.nexperia.lvc157;

import net.common.PinValue;
import net.common.Pins;

public interface LVC1G157Pins
    extends Pins<LVC1G157Snapshot, LVC1G157Pins, LVC1G157>
{
  void setYError();

  void setYUnsettled();

  PinValue getSelector();

  PinValue getEB();

  PinValue getInputValue(int port);

  void setYValue(boolean value);
}

