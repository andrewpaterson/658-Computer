package net.logisim.integratedcircuits.ti.f521;

import net.common.BusValue;
import net.common.PinValue;
import net.integratedcircuits.nexperia.lvc573.LVC573;
import net.integratedcircuits.nexperia.lvc573.LVC573Pins;
import net.integratedcircuits.nexperia.lvc573.LVC573Snapshot;
import net.logisim.common.LogisimPins;

import static net.logisim.integratedcircuits.nexperia.lvc573.LVC573Factory.PORT_Q;

public class F521LogisimPins
    extends LogisimPins<LVC573Snapshot, LVC573Pins, LVC573>
    implements LVC573Pins
{
  @Override
  public PinValue getLE()
  {
    return getValue(F521Factory.PORT_LE);
  }

  @Override
  public BusValue getInput()
  {
    return getValue(F521Factory.PORT_P);
  }

  @Override
  public void setOutputUnsettled()
  {
  }

  @Override
  public void setOutputError()
  {
    setError(PORT_Q);
  }

  @Override
  public void setOutput(long latchValue)
  {
    setValue(PORT_Q, latchValue);
  }

  @Override
  public PinValue getOEB()
  {
    return getValue(F521Factory.PORT_OEB);
  }

  @Override
  public void setOutputHighImpedance()
  {
    setHighImpedance(PORT_Q);
  }
}

