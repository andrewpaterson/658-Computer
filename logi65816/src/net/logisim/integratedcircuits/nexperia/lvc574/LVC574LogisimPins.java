package net.logisim.integratedcircuits.nexperia.lvc574;

import net.common.BusValue;
import net.common.PinValue;
import net.integratedcircuits.nexperia.lvc574.LVC574;
import net.integratedcircuits.nexperia.lvc574.LVC574Pins;
import net.integratedcircuits.nexperia.lvc574.LVC574Snapshot;
import net.logisim.common.LogisimPins;

import static net.logisim.integratedcircuits.nexperia.lvc574.LVC574Factory.*;

public class LVC574LogisimPins
    extends LogisimPins<LVC574Snapshot, LVC574Pins, LVC574>
    implements LVC574Pins
{
  @Override
  public PinValue getClock()
  {
    return getValue(PORT_CP);
  }

  @Override
  public BusValue getInput()
  {
    return getValue(PORT_D);
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
    return getValue(LVC574Factory.PORT_OEB);
  }

  @Override
  public void setOutputHighImpedance()
  {
    setHighImpedance(PORT_Q);
  }
}

