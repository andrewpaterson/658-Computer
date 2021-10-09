package name.bizna.bus.wiring;

import name.bizna.bus.common.Tickables;
import name.bizna.bus.common.Trace;
import name.bizna.bus.common.Uniport;
import name.bizna.bus.logic.Tickable;

public class ClockOscillator
    extends Tickable
{
  private final Uniport out;
  private boolean value;

  public ClockOscillator(Tickables tickables, String name, Trace trace)
  {
    super(tickables, name);
    this.out = new Uniport(this, "Out");
    this.out.connect(trace);
    this.value = false;
  }

  @Override
  public void propagate()
  {
    out.writeBool(value);
  }

  @Override
  public void startPropagation()
  {
    value = !value;
  }

  @Override
  public String getType()
  {
    return "Clock Oscillator";
  }
}

