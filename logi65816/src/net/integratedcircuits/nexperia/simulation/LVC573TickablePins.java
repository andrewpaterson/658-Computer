package net.integratedcircuits.nexperia.simulation;

import net.common.BusValue;
import net.common.PinValue;
import net.integratedcircuits.nexperia.lvc573.LVC573;
import net.integratedcircuits.nexperia.lvc573.LVC573Pins;
import net.integratedcircuits.nexperia.lvc573.LVC573Snapshot;
import net.simulation.common.*;

public class LVC573TickablePins
    extends TickablePins<LVC573Snapshot, LVC573Pins, LVC573>
    implements LVC573Pins
{
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
    super(tickables);
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
    return getPinValue(outputEnableB);
  }

  @Override
  public void setOutputHighImpedance()
  {
    output.highImpedance();
  }
}

