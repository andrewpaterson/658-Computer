package net.logisim.integratedcircuits.common.x4040;

import net.common.PinValue;
import net.integratedcircuits.common.x4040.X4040;
import net.integratedcircuits.common.x4040.X4040Pins;
import net.integratedcircuits.common.x4040.X4040Snapshot;
import net.logisim.common.LogisimPins;

import static net.logisim.integratedcircuits.common.x4040.X4040Factory.*;

public class X4040LogisimPins
    extends LogisimPins<X4040Snapshot, X4040Pins, X4040>
    implements X4040Pins
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

