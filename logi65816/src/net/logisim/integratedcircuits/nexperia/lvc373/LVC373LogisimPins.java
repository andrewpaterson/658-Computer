package net.logisim.integratedcircuits.nexperia.lvc373;

import net.common.BusValue;
import net.common.PinValue;
import net.integratedcircuits.nexperia.lvc373.LVC373;
import net.integratedcircuits.nexperia.lvc373.LVC373Pins;
import net.integratedcircuits.nexperia.lvc373.LVC373Snapshot;
import net.logisim.common.LogisimPins;

import static net.logisim.integratedcircuits.nexperia.lvc373.LVC373Factory.PORT_Q;

public class LVC373LogisimPins
    extends LogisimPins<LVC373Snapshot, LVC373Pins, LVC373>
    implements LVC373Pins
{
  @Override
  public PinValue getLE()
  {
    return getValue(LVC373Factory.PORT_LE);
  }

  @Override
  public BusValue getInput()
  {
    return getValue(LVC373Factory.PORT_D);
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
    return getValue(LVC373Factory.PORT_OEB);
  }

  @Override
  public void setOutputHighImpedance()
  {
    setHighImpedance(PORT_Q);
  }
}

