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
  protected boolean qOut;
  protected boolean previousCP;
  protected boolean previousCEB;

  public LV165(String name, LV165Pins pins)
  {
    super(name, pins);
    q = 0;
    qOut = false;
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

    boolean setQOut = false;

    if ((!plB.isError() && !plB.isNotConnected()) &&
        (!plB.isHigh() ||
         ((!ceB.isError() && !ceB.isNotConnected()) &&
          (!cp.isError() && !cp.isNotConnected()))))
    {
      boolean cpRising = !previousCP && cp.isHigh();
      boolean cebRising = !previousCEB && ceB.isHigh();
      previousCEB = ceB.isHigh();
      previousCP = cp.isHigh();

      if (plB.isLow())
      {
        BusValue parallelData = getPins().getD();
        if (parallelData.isValid())
        {
          q = (int) parallelData.getValue();
          qOut = (q & 0x80) == 0x80;
        }
      }
      else if (plB.isHigh())
      {
        if ((ceB.isLow() && cpRising) ||
            (cp.isLow() && cebRising))
        {
          q = q << 1;
          q = q & 0xFE | (ds.isHigh() ? 1 : 0);
          qOut = (q & 0x80) == 0x80;
        }
        else
        {
          setQOut = true;
        }
      }
    }

    if (setQOut)
    {
      getPins().setQValue(qOut);
    }
  }

  @Override
  public LV165Snapshot createSnapshot()
  {
    return new LV165Snapshot(q,
                             qOut,
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

  public String getValueBinaryString()
  {
    String s = Integer.toBinaryString(q);
    s = StringUtil.pad(8 - s.length(), "0") + s;
    return s;
  }
}

