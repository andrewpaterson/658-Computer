package name.bizna.bus.gate;

import name.bizna.bus.common.Tickables;
import name.bizna.bus.common.Trace;
import name.bizna.bus.common.TraceValue;
import name.bizna.bus.common.Uniport;

public class NotGate
    extends Tickable
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

  public void propagate()
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

