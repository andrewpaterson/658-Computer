package net.integratedcircuits.ti.f283;

import net.common.BusValue;
import net.common.IntegratedCircuit;
import net.common.PinValue;

public class F283
    extends IntegratedCircuit<F283Snapshot, F283Pins>
{
  public static final String TYPE = "4-bit Full Adder";

  protected long latchValue;

  public F283(String name, F283Pins pins)
  {
    super(name, pins);
    latchValue = 0;
  }

  @Override
  public void tick()
  {
    BusValue aValue = getPins().getA();
    BusValue bValue = getPins().getB();
    PinValue c0Value = getPins().getC0();

    if (aValue.isError() || aValue.isNotConnected() ||
        bValue.isError() || bValue.isNotConnected() ||
        c0Value.isError() || c0Value.isNotConnected())
    {
      getPins().setCOError();
      getPins().setSigmaError();
    }
    else
    {
      long c0 = c0Value.getValue();
      long a = aValue.getValue();
      long b = bValue.getValue();

      long sum = a + b + c0;
      getPins().setSigma(sum & 0xf);
      getPins().setCO((sum & 0x10) == 0x10);
    }
  }

  @Override
  public F283Snapshot createSnapshot()
  {
    return new F283Snapshot();
  }

  @Override
  public void restoreFromSnapshot(F283Snapshot snapshot)
  {
  }

  @Override
  public String getType()
  {
    return TYPE;
  }
}

