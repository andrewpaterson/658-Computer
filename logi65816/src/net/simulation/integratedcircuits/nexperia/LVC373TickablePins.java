package net.simulation.integratedcircuits.nexperia;

import net.common.BusValue;
import net.common.PinValue;
import net.integratedcircuits.nexperia.lvc373.LVC373;
import net.integratedcircuits.nexperia.lvc373.LVC373Pins;
import net.integratedcircuits.nexperia.lvc373.LVC373Snapshot;
import net.simulation.common.*;

public class LVC373TickablePins
    extends TickablePins<LVC373Snapshot, LVC373Pins, LVC373>
    implements LVC373Pins
{
  protected Omniport input;
  protected Omniport output;
  protected Uniport outputEnableB;
  protected Uniport latchEnable;

  public LVC373TickablePins(Tickables tickables,
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

