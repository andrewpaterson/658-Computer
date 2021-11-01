package net.simulation.integratedcircuits.nexperia;

import net.common.BusValue;
import net.common.PinValue;
import net.integratedcircuits.nexperia.lvc16373.LVC16373;
import net.integratedcircuits.nexperia.lvc16373.LVC16373Pins;
import net.integratedcircuits.nexperia.lvc16373.LVC16373Snapshot;
import net.simulation.common.*;

public class LVC16373TickablePins
    extends TickablePins<LVC16373Snapshot, LVC16373Pins, LVC16373>
    implements LVC16373Pins
{
  protected Omniport[] input;
  protected Omniport[] output;
  protected Uniport[] outputEnableB;
  protected Uniport[] latchEnable;

  public LVC16373TickablePins(Tickables tickables,
                              Bus inputBus1,
                              Bus inputBus2,
                              Bus outputBus1,
                              Bus outputBus2,
                              Trace outputEnabledBTrace1,
                              Trace outputEnabledBTrace2,
                              Trace latchEnableTrace1,
                              Trace latchEnableTrace2)
  {
    super(tickables);
    int width = 8;
    this.input = new Omniport[2];
    this.input[0] = new Omniport(this, "D1", width);
    this.input[1] = new Omniport(this, "D2", width);
    this.output = new Omniport[2];
    this.output[0] = new Omniport(this, "Q1", width);
    this.output[1] = new Omniport(this, "Q2", width);
    this.outputEnableB = new Uniport[2];
    this.outputEnableB[0] = new Uniport(this, "OE1B");
    this.outputEnableB[1] = new Uniport(this, "OE2B");
    this.latchEnable = new Uniport[2];
    this.latchEnable[0] = new Uniport(this, "LE1");
    this.latchEnable[1] = new Uniport(this, "LE2");

    this.input[0].connect(inputBus1);
    this.input[1].connect(inputBus2);
    this.output[0].connect(outputBus1);
    this.output[1].connect(outputBus2);
    outputEnableB[0].connect(outputEnabledBTrace1);
    outputEnableB[1].connect(outputEnabledBTrace2);
    latchEnable[0].connect(latchEnableTrace1);
    latchEnable[1].connect(latchEnableTrace2);
  }

  @Override
  public PinValue getLE(int index)
  {
    return getPinValue(latchEnable[index]);
  }

  @Override
  public BusValue getInput(int index)
  {
    return getBusValue(input[index]);
  }

  @Override
  public void setOutputUnsettled(int index)
  {
    output[index].unset();
  }

  @Override
  public void setOutputError(int index)
  {
    output[index].error();
  }

  @Override
  public void setOutput(int index, long latchValue)
  {
    output[index].writeAllPinsBool(latchValue);
  }

  @Override
  public PinValue getOEB(int index)
  {
    return getPinValue(outputEnableB[index]);
  }

  @Override
  public void setOutputHighImpedance(int index)
  {
    output[index].highImpedance();
  }
}

