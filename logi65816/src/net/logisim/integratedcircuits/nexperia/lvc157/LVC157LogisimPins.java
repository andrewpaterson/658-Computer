package net.logisim.integratedcircuits.nexperia.lvc157;

import net.common.BusValue;
import net.common.PinValue;
import net.integratedcircuits.nexperia.lvc157.LVC157;
import net.integratedcircuits.nexperia.lvc157.LVC157Pins;
import net.integratedcircuits.nexperia.lvc157.LVC157Snapshot;
import net.logisim.common.LogisimPins;

import static net.logisim.integratedcircuits.nexperia.lvc157.LVC157Factory.*;

public class LVC157LogisimPins
    extends LogisimPins<LVC157Snapshot, LVC157Pins, LVC157>
    implements LVC157Pins
{
  public LVC157LogisimPins()
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
  public void setYHighImpedance()
  {
    setHighImpedance(PORT_Y);
  }

  @Override
  public PinValue getSelector()
  {
    return getValue(PORT_S);
  }

  @Override
  public PinValue getOEB()
  {
    return getValue(PORT_OEB);
  }

  @Override
  public BusValue getInputValue(int port)
  {
    return getValue(PORT_INPUT[port]);
  }

  @Override
  public void setYValue(long value)
  {
    setValue(PORT_Y, value);
  }
}

