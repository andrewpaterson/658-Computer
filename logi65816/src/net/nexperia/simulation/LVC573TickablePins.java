package net.nexperia.simulation;

import net.common.BusValue;
import net.common.IntegratedCircuit;
import net.common.PinValue;
import net.nexperia.lvc573.LVC573;
import net.nexperia.lvc573.LVC573Pins;
import net.nexperia.lvc573.LVC573Snapshot;
import net.simulation.common.*;

public class LVC573TickablePins
    extends Tickable
    implements LVC573Pins
{
  protected LVC573 latch;
  protected LVC573Snapshot snapshot;

  protected Omniport input;
  protected Omniport output;
  protected Uniport outputEnableB;
  protected Uniport latchEnable;

  public LVC573TickablePins(Tickables tickables,
                            String name,
                            int width,
                            Bus inputBus,
                            Bus outputBus,
                            Trace outputEnabledBTrace,
                            Trace latchEnableTrace)
  {
    super(tickables, name);
    new LVC573(this);
    this.input = new Omniport(this, "D", width);
    this.output = new Omniport(this, "Q", width);
    this.outputEnableB = new Uniport(this, "OEB");
    this.latchEnable = new Uniport(this, "LE");

    this.input.connect(inputBus);
    this.output.connect(outputBus);
    outputEnableB.connect(outputEnabledBTrace);
    latchEnable.connect(latchEnableTrace);
  }

  @Override
  public void setLatch(LVC573 latch)
  {
    this.latch = latch;
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
  public PinValue getLE()
  {
    return getPinValue(latchEnable);
  }

  @Override
  public BusValue getInput()
  {
    return getBusValue(input);
  }

  @Override
  public void setOutputUnsettled()
  {
    output.unset();
  }

  @Override
  public void setOutputError()
  {
    output.error();
  }

  @Override
  public void setOutput(long latchValue)
  {
    output.writeAllPinsBool(latchValue);
  }

  @Override
  public PinValue getOEB()
  {
    return null;
  }

  @Override
  public String getType()
  {
    return "Bus Transceiver";
  }

  @Override
  public IntegratedCircuit getIntegratedCircuit()
  {
    return latch;
  }
}

