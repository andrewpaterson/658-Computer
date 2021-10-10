package name.bizna.bus.gate;

import name.bizna.bus.common.Tickables;
import name.bizna.bus.common.Trace;
import name.bizna.bus.common.Uniport;

import java.util.ArrayList;
import java.util.List;

import static name.bizna.bus.common.TraceValue.High;

public class AndGate
    extends LogicGate
{
  protected List<Uniport> in;
  protected Uniport out;

  public AndGate(Tickables tickables, String name, Trace in1, Trace in2, Trace out)
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
    propagateLogic(in, out, High);
  }

  @Override
  public String getType()
  {
    return "AND Gate";
  }
}

