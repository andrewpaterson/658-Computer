package net.logisim.integratedcircuits.ti.lvc543;

import com.cburch.logisim.data.BitWidth;
import com.cburch.logisim.data.Value;
import net.common.BusValue;
import net.common.PinValue;
import net.integratedcircuits.ti.lvc543.LVC543;
import net.integratedcircuits.ti.lvc543.LVC543Pins;
import net.integratedcircuits.ti.lvc543.LVC543Snapshot;
import net.logisim.common.LogisimPins;

public class LVC543LogisimPins
    extends LogisimPins<LVC543Snapshot, LVC543Pins, LVC543>
    implements LVC543Pins
{
  @Override
  public PinValue getLEB(int index)
  {
    return getPinValue(LVC543Factory.PORT_LEB[index]);
  }

  @Override
  public PinValue getCEB(int index)
  {
    return getPinValue(LVC543Factory.PORT_CEB[index]);
  }

  @Override
  public BusValue getInput(int index)
  {
    return getBusValue(LVC543Factory.PORT_IO[index], 8, 2);
  }

  @Override
  public void setOutputUnsettled(int index)
  {
  }

  @Override
  public void setOutputError(int index)
  {
    instanceState.setPort(LVC543Factory.PORT_IO[index], Value.createError(BitWidth.create(8)), 2);
  }

  @Override
  public void setOutput(int index, long latchValue)
  {
    instanceState.setPort(LVC543Factory.PORT_IO[index], Value.createKnown(BitWidth.create(8), latchValue), 2);
  }

  @Override
  public PinValue getOEB(int index)
  {
    return getPinValue(LVC543Factory.PORT_OEB[index]);
  }

  @Override
  public void setOutputHighImpedance(int index)
  {
    instanceState.setPort(LVC543Factory.PORT_IO[index], Value.createUnknown(BitWidth.create(8)), 2);
  }
}

