package net.maxim.logisim;

import com.cburch.logisim.data.Value;
import com.cburch.logisim.std.memory.ClockState;
import net.logisim.common.LogisimPins;
import net.maxim.ds1813.DS1813;
import net.maxim.ds1813.DS1813Pins;
import net.maxim.ds1813.DS1813Snapshot;

public class DS1813LogisimPins
    extends ClockState
    extends LogisimPins<DS1813Snapshot, DS1813Pins, DS1813>
    implements DS1813Pins
{
  @Override
  public void setOut()
  {
    int tickCount = instanceState.getTickCount();
    System.out.println("DS1813LogisimPins.setOut: " + tickCount);
    boolean value = tickCount > 5;
    System.out.println("DS1813LogisimPins.setOut: " + value);
    instanceState.setPort(DS1813Factory.PORT_RSTB, value ? Value.TRUE : Value.FALSE, 20);
  }
}

