package net.logicim.domain.common;

import java.util.List;

import static net.logicim.domain.common.TransmissionState.NotSet;

public abstract class Port
{
  protected Pins pins;
  protected String name;
  protected TransmissionState state;

  protected float highVoltageIn;
  protected float highVoltageOut;
  protected int highToLowPropagationDelay;
  protected int lowToHighPropagationDelay;

  public Port(Pins pins, String name)
  {
    this.pins = pins;
    this.name = name;
    this.state = NotSet;
    pins.addPort(this);
  }

  public String getName()
  {
    return name;
  }

  public void resetConnections()
  {
    state = NotSet;
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

