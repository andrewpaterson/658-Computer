package name.bizna.bus.logic;

import name.bizna.bus.common.Port;

public class NotGate implements Tickable
{
  protected Port in;
  protected Port out;

  protected boolean previousValue;

  public NotGate(Port in, Port out)
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

