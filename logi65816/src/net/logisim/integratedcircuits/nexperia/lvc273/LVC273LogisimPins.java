package net.logisim.integratedcircuits.nexperia.lvc273;

import net.common.BusValue;
import net.common.PinValue;
import net.integratedcircuits.nexperia.lvc273.LVC273;
import net.integratedcircuits.nexperia.lvc273.LVC273Pins;
import net.integratedcircuits.nexperia.lvc273.LVC273Snapshot;
import net.logisim.common.LogisimPins;

import static net.logisim.integratedcircuits.nexperia.lvc273.LVC273Factory.PORT_Q;

public class LVC273LogisimPins
    extends LogisimPins<LVC273Snapshot, LVC273Pins, LVC273>
    implements LVC273Pins
{
  @Override
  public PinValue getCP()
  {
    return getValue(LVC273Factory.PORT_CP);
  }

  @Override
  public BusValue getInput()
  {
    return getValue(LVC273Factory.PORT_D);
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
  public PinValue getMRB()
  {
    return getValue(LVC273Factory.PORT_MRB);
  }

  @Override
  public void setOutputHighImpedance()
  {
    setHighImpedance(PORT_Q);
  }
}

