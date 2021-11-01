package net.simulation.integratedcircuits.ti;

import net.common.BusValue;
import net.common.PinValue;
import net.integratedcircuits.ti.ls148.LS148;
import net.integratedcircuits.ti.ls148.LS148Pins;
import net.integratedcircuits.ti.ls148.LS148Snapshot;
import net.simulation.common.*;

public class LS148TickablePins
    extends TickablePins<LS148Snapshot, LS148Pins, LS148>
    implements LS148Pins
{
  protected Uniport ei;
  protected Omniport input;

  protected Uniport gs;
  protected Omniport a;
  protected Uniport eo;

  public LS148TickablePins(Tickables tickables,
                           Bus inputBus,
                           Trace eiTrace,
                           Bus aBus,
                           Trace gsTrace,
                           Trace eoTrace)
  {
    super(tickables);
    this.input = new Omniport(this, "I", 8);
    this.ei = new Uniport(this, "EI");

    this.gs = new Uniport(this, "GS");
    this.a = new Omniport(this, "A", 3);
    this.eo = new Uniport(this, "EO");

    this.input.connect(inputBus);
    this.a.connect(aBus);
    this.ei.connect(eiTrace);
    this.gs.connect(gsTrace);
    this.eo.connect(eoTrace);
  }

  @Override
  public PinValue getEI()
  {
    return getPinValue(ei);
  }

  @Override
  public BusValue getInput()
  {
    return getBusValue(input);
  }

  @Override
  public void setOutputError()
  {
    a.error();
    eo.error();
    gs.error();
  }

  @Override
  public void setOutputUnsettled()
  {
    a.unset();
    eo.unset();
    gs.unset();
  }

  @Override
  public void setA(long value)
  {
    a.writeAllPinsBool(value);
  }

  @Override
  public void setGS(boolean value)
  {
    gs.writeBool(value);
  }

  @Override
  public void setEO(boolean value)
  {
    eo.writeBool(value);
  }
}

