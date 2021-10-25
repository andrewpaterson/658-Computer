package net.nexperia.simulation;

import net.common.IntegratedCircuit;
import net.nexperia.lvc573.LVC573;
import net.nexperia.lvc573.LVC573Pins;
import net.simulation.common.*;

public class LVC573TickablePins
    extends Tickable
    implements LVC573Pins
{
  protected LVC573 latch;

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
  }

  private void undoPropagation()
  {
  }

  @Override
  public void propagate()
  {
    undoPropagation();
    getIntegratedCircuit().tick();
  }

  @Override
  public void donePropagation()
  {
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

