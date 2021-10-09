package name.bizna.bus.logic;

import name.bizna.bus.common.Tickables;
import name.bizna.bus.common.Trace;
import name.bizna.bus.common.TraceValue;
import name.bizna.bus.common.Uniport;

import java.util.ArrayList;
import java.util.List;

import static name.bizna.bus.common.TraceValue.Error;
import static name.bizna.bus.common.TraceValue.*;

public class OrGate
    extends Tickable
{
  protected List<Uniport> in;
  protected Uniport out;

  public OrGate(Tickables tickables, String name, Trace in1, Trace in2, Trace out)
  {
    super(tickables, name);
    this.in = new ArrayList<>();

    this.in.add(new Uniport(this, "In"));
    this.in.add(new Uniport(this, "In"));
    this.out = new Uniport(this, "Out");

    this.in.get(0).connect(in1);
    this.in.get(1).connect(in2);
    this.out.connect(out);
  }

  public void propagate()
  {
    TraceValue outputValue = Undefined;
    if (in.size() > 0)
    {
      for (Uniport trace : in)
      {
        TraceValue value = trace.readState();
        if (value == Error)
        {
          outputValue = Error;
          break;
        }
        if (value == Undefined)
        {
          outputValue = Undefined;
          break;
        }
        if (value == High)
        {
          outputValue = High;
        }
        else if ((value == Low) && (outputValue == Undefined))
        {
          outputValue = Low;
        }
      }
    }

    out.writeState(outputValue);
  }

  @Override
  public String getType()
  {
    return "OR Gate";
  }
}

