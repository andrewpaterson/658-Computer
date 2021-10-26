package net.nexperia.logisim;

import com.cburch.logisim.data.BitWidth;
import com.cburch.logisim.data.Value;
import net.common.BusValue;
import net.common.PinValue;
import net.logisim.common.LogisimPins;
import net.nexperia.lvc573.LVC573;
import net.nexperia.lvc573.LVC573Pins;
import net.nexperia.lvc573.LVC573Snapshot;

public class LVC573LogisimPins
    extends LogisimPins<LVC573Snapshot, LVC573Pins, LVC573>
    implements LVC573Pins
{
  @Override
  public PinValue getLE()
  {
    return getPinValue(LVC573Factory.PORT_LE);
  }

  @Override
  public BusValue getInput()
  {
    return getBusValue(LVC573Factory.PORT_D, 8, 2);
  }

  @Override
  public void setOutputUnsettled()
  {
  }

  @Override
  public void setOutputError()
  {
    instanceState.setPort(LVC573Factory.PORT_Q, Value.createError(BitWidth.create(8)), 2);
  }

  @Override
  public void setOutput(long latchValue)
  {
    instanceState.setPort(LVC573Factory.PORT_Q, Value.createKnown(BitWidth.create(8), latchValue), 2);
  }

  @Override
  public PinValue getOEB()
  {
    return getPinValue(LVC573Factory.PORT_OEB);
  }
}

