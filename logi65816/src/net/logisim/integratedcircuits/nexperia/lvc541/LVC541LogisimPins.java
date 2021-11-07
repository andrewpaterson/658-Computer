package net.logisim.integratedcircuits.nexperia.lvc541;

import net.common.BusValue;
import net.common.PinValue;
import net.integratedcircuits.nexperia.lvc541.LVC541;
import net.integratedcircuits.nexperia.lvc541.LVC541Pins;
import net.integratedcircuits.nexperia.lvc541.LVC541Snapshot;
import net.logisim.common.LogisimPins;

import static net.logisim.integratedcircuits.nexperia.lvc541.LVC541Factory.*;

public class LVC541LogisimPins
    extends LogisimPins<LVC541Snapshot, LVC541Pins, LVC541>
    implements LVC541Pins
{
  public LVC541LogisimPins()
  {
  }

  @Override
  public void setYError()
  {
    setError(PORT_A);
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
  public void setYValue(long value)
  {
    setValue(PORT_Y, value);
  }

  @Override
  public PinValue getOE1B()
  {
    return getValue(PORT_OE1B);
  }

  @Override
  public PinValue getOE2B()
  {
    return getValue(PORT_OE2B);
  }

  @Override
  public BusValue getAValue()
  {
    return getValue(PORT_A);
  }
}

