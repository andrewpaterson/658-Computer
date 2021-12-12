package net.integratedcircuits.nexperia.lv165;

import net.common.BusValue;
import net.common.IntegratedCircuit;
import net.common.PinValue;
import net.util.StringUtil;

import static net.util.IntUtil.toByte;

public class LV165
    extends IntegratedCircuit<LV165Snapshot, LV165Pins>
{
  public static final String TYPE = "8-bit parallel in shift";

  protected int q;
  protected boolean previousCP;
  protected boolean previousCEB;

  public LV165(String name, LV165Pins pins)
  {
    super(name, pins);
    q = 0;
    previousCEB = false;
    previousCEB = false;
  }

  @Override
  public void tick()
  {
    PinValue ceB = getPins().getCEB();
    PinValue cp = getPins().getCP();
    PinValue plB = getPins().getPLB();
    PinValue ds = getPins().getDS();

    if ((!plB.isError() && !plB.isNotConnected()) &&
        (!plB.isHigh() ||
         ((!ceB.isError() && !ceB.isNotConnected()) &&
          (!cp.isError() && !cp.isNotConnected()))))
    {
      if (plB.isLow())
      {
        BusValue parallelData = getPins().getD();
        if (parallelData.isValid())
        {
          q = (int) parallelData.getValue();
        }
      }
      else if (plB.isHigh())
      {
        boolean cpRising = !previousCP && cp.isHigh();
        boolean cebRising = !previousCEB && ceB.isHigh();

        if ((ceB.isLow() && cpRising) ||
            (cp.isLow() && cebRising))
        {
          q = q << 1;
          q = q & 0xFE | (ds.isHigh() ? 1 : 0);
        }
      }
    }

    previousCEB = ceB.isHigh();
    previousCP = cp.isHigh();
    getPins().setQValue((q & 0x80) == 0x80);
  }

  @Override
  public LV165Snapshot createSnapshot()
  {
    return new LV165Snapshot(q,
                             previousCP,
                             previousCEB);
  }

  @Override
  public void restoreFromSnapshot(LV165Snapshot snapshot)
  {
    q = snapshot.q;
    previousCP = snapshot.previousCP;
    previousCEB = snapshot.previousCEB;
  }

  @Override
  public String getType()
  {
    return TYPE;
  }

  public String getValueString()
  {
    return StringUtil.getByteStringHex(toByte(q));
  }
}

