package net.logicim.domain.common.port;

import net.common.SimulatorException;
import net.logicim.domain.Simulation;
import net.logicim.domain.common.Described;
import net.logicim.domain.common.wire.Trace;

import java.util.Set;

public abstract class Port
    implements Described
{
  protected PortType type;
  protected String name;
  protected Trace trace;
  protected PortHolder holder;
  protected boolean explicit;

  public Port(PortType type,
              String name,
              PortHolder holder)
  {
    this.type = type;
    this.name = name;
    this.holder = holder;
    this.trace = null;
    this.holder.addPort(this);
    this.explicit = true;
  }

  public String getName()
  {
    return name;
  }

  public Trace getTrace()
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

  public String toDebugString()
  {
    String holderName = getHolder().getName();
    if (holderName == null)
    {
      holderName = "";
    }
    return holderName + "." + this.name + "(" + getTraceId() + ")";
  }

  public String getDescription()
  {
    return getHolder().getComponent().getDescription() + "." + getName();
  }

  public boolean isLogicPort()
  {
    return false;
  }

  public boolean isPowerIn()
  {
    return false;
  }

  public void connect(Trace trace, boolean explicit)
  {
    this.explicit = explicit;
    connect(trace);
  }

  public void connect(Trace trace)
  {
    if (this.trace != null)
    {
      throw new SimulatorException("Port [%s] is already connected.", getName());
    }

    if (trace == null)
    {
      throw new SimulatorException("Cannot connect null Trace to Port [%s].", getName());
    }

    this.trace = trace;
    this.trace.connect(this);
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

  public void reset()
  {
  }

  public String getType()
  {
    return getClass().getSimpleName().replace("Port", "");
  }

  public boolean isExplicit()
  {
    return explicit;
  }
}

