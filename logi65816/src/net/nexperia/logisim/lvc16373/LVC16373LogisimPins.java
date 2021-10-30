package net.nexperia.logisim.lvc16373;

import com.cburch.logisim.data.BitWidth;
import com.cburch.logisim.data.Value;
import net.common.BusValue;
import net.common.PinValue;
import net.logisim.common.LogisimPins;
import net.nexperia.lvc16373.LVC16373;
import net.nexperia.lvc16373.LVC16373Pins;
import net.nexperia.lvc16373.LVC16373Snapshot;

public class LVC16373LogisimPins
    extends LogisimPins<LVC16373Snapshot, LVC16373Pins, LVC16373>
    implements LVC16373Pins
{
  @Override
  public PinValue getLE(int index)
  {
    return getPinValue(LVC16373Factory.PORT_LE[index]);
  }

  @Override
  public BusValue getInput(int index)
  {
    return getBusValue(LVC16373Factory.PORT_D[index], 8, 2);
  }

  @Override
  public void setOutputUnsettled(int index)
  {
  }

  @Override
  public void setOutputError(int index)
  {
    instanceState.setPort(LVC16373Factory.PORT_Q[index], Value.createError(BitWidth.create(8)), 2);
  }

  @Override
  public void setOutput(int index, long latchValue)
  {
    instanceState.setPort(LVC16373Factory.PORT_Q[index], Value.createKnown(BitWidth.create(8), latchValue), 2);
  }

  @Override
  public PinValue getOEB(int index)
  {
    return getPinValue(LVC16373Factory.PORT_OEB[index]);
  }

  @Override
  public void setOutputHighImpedance(int index)
  {
    instanceState.setPort(LVC16373Factory.PORT_Q[index], Value.createUnknown(BitWidth.create(8)), 2);
  }
}

