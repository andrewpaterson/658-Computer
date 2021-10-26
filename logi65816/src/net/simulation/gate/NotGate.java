package net.simulation.gate;

import net.simulation.common.Tickables;
import net.simulation.common.Trace;
import net.simulation.common.TraceValue;
import net.simulation.common.Uniport;

public class NotGate
    extends LogicGate
{
  protected Uniport in;
  protected Uniport out;

  public NotGate(Tickables tickables, String name, Trace inTrace, Trace outTrace)
  {
    super(tickables, name);
    in = new Uniport(this, "In");
    out = new Uniport(this, "Out");

    in.connect(inTrace);
    out.connect(outTrace);
  }

  public void tick()
  {
    TraceValue inValue = in.read();
    if (inValue.isError() || inValue.isNotConnected())
    {
      out.error();
    }
    else if (inValue.isUnsettled())
    {
      out.unset();
    }
    else
    {
      boolean calculatedValue = !inValue.isHigh();
      out.writeBool(calculatedValue);
    }
  }

  @Override
  public String getType()
  {
    return "NOT Gate";
  }
}

