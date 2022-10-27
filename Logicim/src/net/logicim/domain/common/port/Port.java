package net.logicim.domain.common.port;

import net.logicim.domain.common.Pins;
import net.logicim.domain.common.TransmissionState;
import net.logicim.domain.common.propagation.Propagation;

import static net.logicim.domain.common.TransmissionState.NotSet;

public abstract class Port
{
  protected PortType type;
  protected Pins pins;
  protected String name;
  protected TransmissionState state;
  protected Propagation propagation;

  public Port(PortType type,
              Pins pins,
              String name,
              Propagation propagation)
  {
    this.type = type;
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

  public boolean isInput()
  {
    return type == PortType.Input ||
           type == PortType.Bidirectional ||
           type == PortType.Passive ||
           type == PortType.PowerIn;
  }
}

