package net.integratedcircuits.nexperia.lvc595;

import net.common.IntegratedCircuit;
import net.common.PinValue;
import net.util.StringUtil;

import static net.util.IntUtil.toByte;

public class LVC595
    extends IntegratedCircuit<LVC595Snapshot, LVC595Pins>
{
  public static final String TYPE = "8-bit serial in shift";

  protected int shiftRegister;
  protected boolean qs7;
  protected int storageRegister;
  protected boolean prevSHCP;
  protected boolean prevSTCP;

  public LVC595(String name, LVC595Pins pins)
  {
    super(name, pins);
    shiftRegister = 0;
    storageRegister = 0;
    qs7 = false;
    prevSHCP = false;
    prevSTCP = false;
  }

  @Override
  public void tick()
  {
    PinValue oeB = getPins().getOEB();
    PinValue mrB = getPins().getMRB();
    PinValue ds = getPins().getDS();
    PinValue shcp = getPins().getSHCP();
    PinValue stcp = getPins().getSTCP();

    boolean stcpRising = !prevSTCP && stcp.isHigh();
    boolean shcpRising = !prevSHCP && shcp.isHigh();
    boolean shcpFalling = prevSHCP && !shcp.isHigh();
    boolean highImpedance = false;

    if ((oeB.isError() || oeB.isNotConnected()) ||
        (mrB.isError() || mrB.isNotConnected()) ||
        ds.isError() ||
        stcp.isError() ||
        shcp.isError())
    {
      getPins().setQError();
    }
    else
    {
      if (oeB.isHigh())
      {
        highImpedance = true;
      }

      if (mrB.isLow())
      {
        shiftRegister = 0;
      }
    }

    if (stcpRising)
    {
      storageRegister = shiftRegister;
    }

    if (shcpRising)
    {
      shift(ds);
    }
    if (shcpFalling)
    {
      qs7 = (shiftRegister & 0x80) == 0x80;
      getPins().setQ7S(qs7);
    }

    if (!highImpedance)
    {
      getPins().setQ(storageRegister);
    }
    else
    {
      getPins().setQImpedance();
    }

    prevSTCP = stcp.isHigh();
    prevSHCP = shcp.isHigh();
  }

  private void shift(PinValue ds)
  {
    shiftRegister = shiftRegister << 1;
    shiftRegister = shiftRegister & 0xFE | (ds.isHigh() ? 1 : 0);
  }

  @Override
  public LVC595Snapshot createSnapshot()
  {
    return new LVC595Snapshot(shiftRegister,
                              storageRegister,
                              prevSHCP,
                              prevSTCP,
                              qs7);
  }

  @Override
  public void restoreFromSnapshot(LVC595Snapshot snapshot)
  {
    shiftRegister = snapshot.shiftRegister;
    qs7 = snapshot.qs7;
    storageRegister = snapshot.storageRegister;
    prevSHCP = snapshot.prevSHCP;
    prevSTCP = snapshot.prevSTCP;
  }

  @Override
  public String getType()
  {
    return TYPE;
  }

  public String getShiftValueString()
  {
    return StringUtil.getByteStringHex(toByte(shiftRegister));
  }

  public String getStorageValueString()
  {
    return StringUtil.getByteStringHex(toByte(storageRegister));
  }
}

