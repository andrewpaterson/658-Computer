package name.bizna.cpu;

import name.bizna.bus.common.Single;
import name.bizna.bus.logic.Tickable;

public class ClockOscillator
    implements Tickable
{
  private Single clockTrace;
  private boolean value;
  private boolean hasSettled;

  public ClockOscillator(Single clockTrace)
  {
    this.clockTrace = clockTrace;
    this.value = clockTrace.get();
    this.hasSettled = false;
  }

  @Override
  public boolean propagate()
  {
    if (hasSettled)
    {
      hasSettled = false;
      value = !value;
    }
    clockTrace.set(value);
    return true;
  }

  @Override
  public void donePropagation()
  {
    hasSettled = true;
  }
}

