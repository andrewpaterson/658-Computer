package net.logisim.integratedcircuits.nexperia.lvc573;

import com.cburch.logisim.data.BitWidth;
import com.cburch.logisim.data.Value;
import net.common.BusValue;
import net.common.PinValue;
import net.integratedcircuits.nexperia.lvc573.LVC573;
import net.integratedcircuits.nexperia.lvc573.LVC573Pins;
import net.integratedcircuits.nexperia.lvc573.LVC573Snapshot;
import net.logisim.common.LogiBus;
import net.logisim.common.LogisimPins;

import static net.logisim.integratedcircuits.nexperia.lvc573.LVC573Factory.PORT_Q;

public class LVC573LogisimPins
    extends LogisimPins<LVC573Snapshot, LVC573Pins, LVC573>
    implements LVC573Pins
{
  @Override
  public PinValue getLE()
  {
    return getValue(LVC573Factory.PORT_LE);
  }

  @Override
  public BusValue getInput()
  {
    return getValue(LVC573Factory.PORT_D);
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
    return getValue(LVC573Factory.PORT_OEB);
  }

  @Override
  public void setOutputHighImpedance()
  {
    setHighImpedance(PORT_Q);
  }
}

