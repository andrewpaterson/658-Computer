package name.bizna.bus.logic;

import name.bizna.bus.common.Port;

import java.util.ArrayList;
import java.util.List;

public class OrGate
    implements Tickable
{
  protected List<Port> in;
  protected Port out;

  protected boolean outValue;

  public OrGate(Port in1, Port in2, Port out)
  {
    this.in = new ArrayList<>();
    this.in.add(in1);
    this.in.add(in2);
    this.out = out;
  }

  public boolean propagate()
  {
    boolean newOutValue;
    if (in.size() > 0)
    {
      boolean value = false;
      for (Port trace : in)
      {
        value = value | trace.get();
      }

      newOutValue = value;
      out.set(newOutValue);
    }
    else
    {
      newOutValue = true;
      out.set(newOutValue);
    }

    boolean settled = newOutValue == outValue;
    this.outValue = newOutValue;
    return settled;
  }
}

