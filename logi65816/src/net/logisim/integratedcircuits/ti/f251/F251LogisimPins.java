package net.logisim.integratedcircuits.ti.f251;

import net.common.BusValue;
import net.common.PinValue;
import net.integratedcircuits.ti.f251.F251;
import net.integratedcircuits.ti.f251.F251Pins;
import net.integratedcircuits.ti.f251.F251Snapshot;
import net.logisim.common.LogisimPins;

import static net.logisim.integratedcircuits.ti.f251.F251Factory.*;

public class F251LogisimPins
    extends LogisimPins<F251Snapshot, F251Pins, F251>
    implements F251Pins
{
  @Override
  public PinValue getGB()
  {
    return getValue(PORT_GB);
  }

  @Override
  public BusValue getA()
  {
    return getValue(PORT_A);
  }

  @Override
  public BusValue getD()
  {
    return getValue(PORT_D);
  }

  @Override
  public void setOutputError()
  {
    setError(PORT_Y);
    setError(PORT_W);
  }

  @Override
  public void setOutputUnsettled()
  {
  }

  @Override
  public void setOutputHighImpedance()
  {
    setHighImpedance(PORT_Y);
    setHighImpedance(PORT_W);
  }

  @Override
  public void setOutput(boolean yValue, boolean wValue)
  {
    setValue(PORT_Y, yValue);
    setValue(PORT_W, wValue);
  }
}

