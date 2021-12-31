package net.logisim.integratedcircuits.maxim.ds1813;

import net.common.PinValue;
import net.integratedcircuits.maxim.ds1813.DS1813;
import net.integratedcircuits.maxim.ds1813.DS1813Pins;
import net.integratedcircuits.maxim.ds1813.DS1813Snapshot;
import net.logisim.common.LogisimPins;
import net.util.StringUtil;

import static net.logisim.integratedcircuits.maxim.ds1813.DS1813Factory.PORT_RESET_SW;
import static net.logisim.integratedcircuits.maxim.ds1813.DS1813Factory.PORT_RSTB;

public class DS1813LogisimPins
    extends LogisimPins<DS1813Snapshot, DS1813Pins, DS1813>
    implements DS1813Pins
{
  public String resetString;

  private long resetTickTarget;

  public DS1813LogisimPins()
  {
    this.resetTickTarget = 0x7FFFFFFFFFFFFFFFL;
  }

  @Override
  public void setOut()
  {
    long tickCount = instanceState.getTickCount();
    if (resetTickTarget == 0x7FFFFFFFFFFFFFFFL)
    {
      resetTickTarget = tickCount + getIntegratedCircuit().getResetTickCount();
    }

    DS1813 econoReset = getIntegratedCircuit();
    boolean value = tickCount > resetTickTarget;
    setValue(PORT_RSTB, value);

    int resetTickCount = econoReset.getResetTickCount();
    long width = Math.min(resetTickCount - (resetTickTarget - tickCount), resetTickCount + 1);
    resetString = StringUtil.pad((int) (width / econoReset.getStretch()), "-");
  }

  @Override
  public PinValue getReset()
  {
    return getValue(PORT_RESET_SW);
  }

  @Override
  public void startCounter()
  {
    this.resetTickTarget = 0x7FFFFFFFFFFFFFFFL;
  }

  public String getResetString()
  {
    return resetString != null ? resetString : "";
  }
}

