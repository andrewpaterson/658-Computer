package net.logicim.domain.common;

import net.logicim.domain.common.propagation.Propagation;
import net.logicim.domain.common.trace.Trace;

import java.util.List;

import static net.logicim.domain.common.TransmissionState.NotSet;

public abstract class Port
{
  protected Pins pins;
  protected String name;
  protected TransmissionState state;

  protected Propagation propagation;

  public Port(Pins pins, String name, Propagation propagation)
  {
    this.pins = pins;
    this.name = name;
    this.propagation = propagation;
    this.state = NotSet;
    pins.addPort(this);
  }

  public String getName()
  {
    return name;
  }

  public String getPortTransmissionStateAsString()
  {
    String portStateString = "  ";
    if (state.isInput())
    {
      portStateString = "<-";
    }
    else if (state.isOutput())
    {
      portStateString = "->";
    }
    else if (state.isNotSet())
    {
      portStateString = "..";
    }
    else if (state.isImpedance())
    {
      portStateString = "xx";
    }
    return getName() + "[" + portStateString + "]";
  }

  public String getDescription()
  {
    return getPins().getDescription() + "." + getName();
  }

  public Pins getPins()
  {
    return pins;
  }

  public abstract List<Trace> getConnections();
}

