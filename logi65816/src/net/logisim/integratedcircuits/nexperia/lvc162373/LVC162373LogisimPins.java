package net.logisim.integratedcircuits.nexperia.lvc162373;

import net.common.BusValue;
import net.common.PinValue;
import net.integratedcircuits.nexperia.lvc162373.LVC162373;
import net.integratedcircuits.nexperia.lvc162373.LVC162373Pins;
import net.integratedcircuits.nexperia.lvc162373.LVC162373Snapshot;
import net.logisim.common.LogisimPins;

import static net.logisim.integratedcircuits.nexperia.lvc162373.LVC162373Factory.*;

public class LVC162373LogisimPins
    extends LogisimPins<LVC162373Snapshot, LVC162373Pins, LVC162373>
    implements LVC162373Pins
{
  @Override
  public PinValue getLE(int index)
  {
    return getValue(PORT_LE[index]);
  }

  @Override
  public BusValue getInput(int index)
  {
    return getValue(PORT_D[index]);
  }

  @Override
  public void setOutputUnsettled(int index)
  {
  }

  @Override
  public void setOutputError(int index)
  {
    setError(PORT_Q[index]);
  }

  @Override
  public void setOutput(int index, long latchValue)
  {
    setValue(PORT_Q[index], latchValue);
  }

  @Override
  public PinValue getOEB(int index)
  {
    return getValue(PORT_OEB[index]);
  }

  @Override
  public void setOutputHighImpedance(int index)
  {
    setHighImpedance(PORT_Q[index]);
  }
}

