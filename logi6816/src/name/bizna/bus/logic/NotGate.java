package name.bizna.bus.logic;

import name.bizna.bus.common.Single;

public class NotGate implements Tickable
{
  protected Single in;
  protected Single out;

  protected boolean previousValue;

  public NotGate(Single in, Single out)
  {
    this.in = in;
    this.out = out;
  }

  public boolean propagate()
  {
    boolean calculatedValue = !in.get();
    out.set(calculatedValue);

    boolean settled = calculatedValue == previousValue;
    this.previousValue = calculatedValue;
    return settled;
  }
}

