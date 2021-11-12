package net.logisim.integratedcircuits.maxim.ds1813;

import com.cburch.logisim.data.Value;
import net.integratedcircuits.maxim.ds1813.DS1813;
import net.integratedcircuits.maxim.ds1813.DS1813Pins;
import net.integratedcircuits.maxim.ds1813.DS1813Snapshot;
import net.logisim.common.LogisimPins;
import net.util.StringUtil;

public class DS1813LogisimPins
    extends LogisimPins<DS1813Snapshot, DS1813Pins, DS1813>
    implements DS1813Pins
{
  public String resetString;

  @Override
  public void setOut()
  {
    int tickCount = instanceState.getTickCount();
    DS1813 econoReset = getIntegratedCircuit();
    int resetTickCount = econoReset.getResetTickCount();
    boolean value = tickCount > resetTickCount;
    instanceState.setPort(DS1813Factory.PORT_RSTB, value ? Value.TRUE : Value.FALSE, 20);

    int width = Math.min(tickCount, resetTickCount + 1);
    resetString = StringUtil.pad(width / econoReset.getStretch(), "-");

  }

  public String getResetString()
  {
    return resetString != null ? resetString : "";
  }
}

