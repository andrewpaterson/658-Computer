package net.logisim.integratedcircuits.ti.f283;

import net.common.BusValue;
import net.common.PinValue;
import net.integratedcircuits.ti.f283.F283;
import net.integratedcircuits.ti.f283.F283Pins;
import net.integratedcircuits.ti.f283.F283Snapshot;
import net.logisim.common.LogisimPins;

import static net.logisim.integratedcircuits.ti.f283.F283Factory.*;

public class F283LogisimPins
    extends LogisimPins<F283Snapshot, F283Pins, F283>
    implements F283Pins
{
  @Override
  public BusValue getA()
  {
    return getValue(PORT_A);
  }

  @Override
  public BusValue getB()
  {
    return getValue(PORT_B);
  }

  @Override
  public PinValue getC0()
  {
    return getValue(PORT_C0);
  }

  @Override
  public void setCOError()
  {
    setError(PORT_CO);
  }

  @Override
  public void setCOUnsettled()
  {
  }

  @Override
  public void setCO(boolean value)
  {
    setValue(PORT_CO, value);
  }

  @Override
  public void setSigmaError()
  {
    setError(PORT_SIGMA);
  }

  @Override
  public void setSigmaUnsettled()
  {
  }

  @Override
  public void setSigma(long value)
  {
    setValue(PORT_SIGMA, value);
  }
}

