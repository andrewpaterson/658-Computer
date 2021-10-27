package net.maxim.logisim;

import com.cburch.logisim.data.Value;
import net.logisim.common.LogisimPins;
import net.maxim.ds1813.DS1813;
import net.maxim.ds1813.DS1813Pins;
import net.maxim.ds1813.DS1813Snapshot;

public class DS1813LogisimPins
    extends LogisimPins<DS1813Snapshot, DS1813Pins, DS1813>
    implements DS1813Pins
{
  @Override
  public void setOut(boolean value)
  {
    instanceState.setPort(DS1813Factory.PORT_RSTB, value ? Value.TRUE : Value.FALSE, 20);
  }
}

