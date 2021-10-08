package name.bizna.bus.logic;

import name.bizna.bus.common.Tickables;
import name.bizna.bus.common.Trace;
import name.bizna.bus.common.Uniport;

public class NotGate
    extends Tickable
{
  protected Uniport in;
  protected Uniport out;

  protected boolean previousValue;

  public NotGate(Tickables tickables, Trace inTrace, Trace outTrace)
  {
    super(tickables);
    in = new Uniport(this);
    out = new Uniport(this);

    in.connect(inTrace);
    out.connect(outTrace);
  }

  public void propagate()
  {
    boolean calculatedValue = !in.readBool();
    out.writeBool(calculatedValue);

    boolean settled = calculatedValue == previousValue;
    this.previousValue = calculatedValue;
  }
}

