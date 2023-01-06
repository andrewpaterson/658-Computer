package net.logicim.domain.common.port;

import net.logicim.common.SimulatorException;
import net.logicim.domain.Simulation;
import net.logicim.domain.common.trace.TraceNet;

import java.util.Set;

public abstract class Port
{
  protected PortType type;
  protected String name;
  protected TraceNet trace;
  protected PortHolder holder;

  public Port(PortType type, String name, PortHolder holder)
  {
    this.type = type;
    this.name = name;
    this.holder = holder;
    this.holder.addPort(this);
  }

  public String getName()
  {
    return name;
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

  public abstract String toDebugString();

  public String getDescription()
  {
    return getHolder().getDescription() + "." + getName();
  }

  public boolean isLogicPort()
  {
    return false;
  }

  public boolean isPowerOut()
  {
    return false;
  }

  public boolean isPowerIn()
  {
    return false;
  }

  public void connect(TraceNet trace)
  {
    if (this.trace == null)
    {
      if (trace != null)
      {
        this.trace = trace;
        this.trace.connect(this);
      }
    }
    else
    {
      throw new SimulatorException("Port [" + getName() + "] is already connected.");
    }
  }

  public Set<Port> getConnectedPorts()
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

  public PortHolder getHolder()
  {
    return holder;
  }

  public void traceConnected(Simulation simulation)
  {
    holder.traceConnected(simulation, this);
  }

  public abstract void reset();
}

