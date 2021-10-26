package net.nexperia.logisim;

import com.cburch.logisim.data.BitWidth;
import com.cburch.logisim.data.Value;
import net.common.BusValue;
import net.common.IntegratedCircuit;
import net.common.PinValue;
import net.logisim.common.LogisimPins;
import net.nexperia.lvc4245.LVC4245;
import net.nexperia.lvc573.LVC573;
import net.nexperia.lvc573.LVC573Pins;
import net.nexperia.lvc573.LVC573Snapshot;

public class LVC573LogisimPins
    extends LogisimPins
    implements LVC573Pins
{
  protected LVC573 latch;
  protected LVC573Snapshot snapshot;

  public LVC573LogisimPins()
  {
    new LVC573(this);
  }

  @Override
  public void startPropagation()
  {
    snapshot = latch.createSnapshot();
  }

  @Override
  public void undoPropagation()
  {
    if (snapshot != null)
    {
      latch.restoreFromSnapshot(snapshot);
    }
  }

  @Override
  public void donePropagation()
  {
    snapshot = null;
  }

  @Override
  public IntegratedCircuit getIntegratedCircuit()
  {
    return latch;
  }

  @Override
  public void setLatch(LVC573 latch)
  {
    this.latch = latch;
  }

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

  public LVC573 getLatch()
  {
    return latch;
  }
}

