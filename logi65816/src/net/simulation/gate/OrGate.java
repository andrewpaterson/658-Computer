package net.simulation.gate;

import net.simulation.common.Tickables;
import net.simulation.common.Trace;
import net.simulation.common.Uniport;

import java.util.ArrayList;
import java.util.List;

import static net.simulation.common.TraceValue.Low;

public class OrGate
    extends LogicGate
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

  public void tick()
  {
    propagateLogic(in, out, Low);
  }

  @Override
  public String getType()
  {
    return "OR Gate";
  }
}

