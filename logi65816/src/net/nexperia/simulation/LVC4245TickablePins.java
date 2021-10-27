package net.nexperia.simulation;

import net.common.BusValue;
import net.common.PinValue;
import net.nexperia.lvc4245.LVC4245;
import net.nexperia.lvc4245.LVC4245Pins;
import net.nexperia.lvc4245.LVC4245Snapshot;
import net.simulation.common.*;

public class LVC4245TickablePins
    extends Tickable<LVC4245Snapshot, LVC4245Pins, LVC4245>
    implements LVC4245Pins
{
  protected LVC4245 transceiver;

  protected Omniport[] ports;
  protected Uniport outputEnableB;
  protected Uniport dir;

  public LVC4245TickablePins(Tickables tickables,
                             String name,
                             int width,
                             Bus aBus,
                             Bus bBus,
                             Trace outputEnabledBTrace,
                             Trace dirTrace)
  {
    super(tickables);
    this.ports = new Omniport[2];
    this.ports[PORT_A_INDEX] = new Omniport(this, "A", width);
    this.ports[PORT_B_INDEX] = new Omniport(this, "B", width);
    this.outputEnableB = new Uniport(this, "OEB");
    this.dir = new Uniport(this, "DIR");

    this.ports[PORT_A_INDEX].connect(aBus);
    this.ports[PORT_B_INDEX].connect(bBus);
    outputEnableB.connect(outputEnabledBTrace);
    dir.connect(dirTrace);
  }

  @Override
  public void setPortError(int index)
  {
    ports[index].error();
  }

  @Override
  public void setPortUnsettled(int index)
  {
    ports[index].unset();
  }

  @Override
  public void setPortHighImpedance(int index)
  {
    ports[index].highImpedance();
  }

  @Override
  public PinValue getDir()
  {
    return getPinValue(dir);
  }

  @Override
  public PinValue getOEB()
  {
    return getPinValue(outputEnableB);
  }

  @Override
  public BusValue getPortValue(int index)
  {
    return getBusValue(ports[index]);
  }

  @Override
  public void setPortValue(int index, long value)
  {
    ports[index].writeAllPinsBool(value);
  }
}

