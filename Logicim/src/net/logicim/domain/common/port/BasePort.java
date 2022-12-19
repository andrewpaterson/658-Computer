package net.logicim.domain.common.port;

import net.logicim.common.SimulatorException;
import net.logicim.domain.Simulation;
import net.logicim.domain.common.Pins;
import net.logicim.domain.common.trace.TraceNet;

import java.util.Set;

public abstract class BasePort
{
  protected PortType type;
  protected String name;
  protected Pins pins;
  protected TraceNet trace;

  public BasePort(PortType type, String name, Pins pins)
  {
    this.type = type;
    this.name = name;
    this.pins = pins;
  }

  public String getName()
  {
    return name;
  }

  public Pins getPins()
  {
    return pins;
  }

  public String toDebugString()
  {
    return getPins().getIntegratedCircuit().getName() + "." + name;
  }

  public String getDescription()
  {
    return getPins().getDescription() + "." + getName();
  }

  public TraceNet getTrace()
  {
    return trace;
  }

  public long getTraceId()
  {
    if (trace != null)
    {
      return trace.getId();
    }
    return 0;
  }

  public void disconnect(Simulation simulation)
  {
    if (trace != null)
    {
      trace.disconnect(this);
      trace = null;
    }
  }

  public boolean isLogicPort()
  {
    return false;
  }

  public boolean isPowerOut()
  {
    return false;
  }

  public void connect(TraceNet trace)
  {
    if (this.trace == null)
    {
      this.trace = trace;
      trace.connect(this);
    }
    else
    {
      throw new SimulatorException("Port [" + getName() + "] is already connected.");
    }
  }

  public Set<BasePort> getConnectedPorts()
  {
    if (trace != null)
    {
      return trace.getConnectedPorts();
    }
    else
    {
      return null;
    }
  }

  public abstract void reset();

  public void traceConnected(Simulation simulation)
  {
  }
}

