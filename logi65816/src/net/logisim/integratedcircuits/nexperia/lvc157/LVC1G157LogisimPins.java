package net.logisim.integratedcircuits.nexperia.lvc157;

import net.common.BusValue;
import net.common.PinValue;
import net.integratedcircuits.nexperia.lvc157.LVC1G157;
import net.integratedcircuits.nexperia.lvc157.LVC1G157Pins;
import net.integratedcircuits.nexperia.lvc157.LVC1G157Snapshot;
import net.logisim.common.LogisimPins;

import static net.logisim.integratedcircuits.nexperia.lvc157.LVC1G157Factory.*;

public class LVC1G157LogisimPins
    extends LogisimPins<LVC1G157Snapshot, LVC1G157Pins, LVC1G157>
    implements LVC1G157Pins
{
  public LVC1G157LogisimPins()
  {
  }

  @Override
  public void setYError()
  {
    setError(PORT_Y);
  }

  @Override
  public void setYUnsettled()
  {
  }

  @Override
  public PinValue getSelector()
  {
    return getValue(PORT_S);
  }

  @Override
  public PinValue getEB()
  {
    return getValue(PORT_EB);
  }

  @Override
  public PinValue getInputValue(int port)
  {
    return getValue(PORT_INPUT[port]);
  }

  @Override
  public void setYValue(boolean value)
  {
    setValue(PORT_Y, value);
  }
}

