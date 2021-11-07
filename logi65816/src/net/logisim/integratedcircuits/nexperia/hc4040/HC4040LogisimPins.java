package net.logisim.integratedcircuits.nexperia.hc4040;

import net.common.PinValue;
import net.integratedcircuits.nexperia.hc4040.HC4040;
import net.integratedcircuits.nexperia.hc4040.HC4040Pins;
import net.integratedcircuits.nexperia.hc4040.HC4040Snapshot;
import net.logisim.common.LogisimPins;

import static net.logisim.integratedcircuits.nexperia.hc4040.HC4040Factory.*;

public class HC4040LogisimPins
    extends LogisimPins<HC4040Snapshot, HC4040Pins, HC4040>
    implements HC4040Pins
{
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
  public PinValue getMasterResetB()
  {
    return getValue(PORT_MR);
  }

  @Override
  public PinValue getClock()
  {
    return getValue(PORT_CP);
  }
}

