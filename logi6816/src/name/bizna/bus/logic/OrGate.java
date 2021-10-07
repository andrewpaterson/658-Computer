package name.bizna.bus.logic;

import name.bizna.bus.common.Single;

import java.util.ArrayList;
import java.util.List;

public class OrGate
    implements Tickable
{
  protected List<Single> in;
  protected Single out;

  protected boolean outValue;

  public OrGate(Single in1, Single in2, Single out)
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
      for (Single single : in)
      {
        value = value | single.get();
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

