package net.logisim.common;

import com.cburch.logisim.data.BitWidth;
import com.cburch.logisim.data.Value;
import com.cburch.logisim.instance.InstanceState;
import net.common.PinValue;

public class LogiPin
{
  public int index;
  public int propagationDelay;

  public LogiPin(int index, int propagationDelay)
  {
    this.index = index;
    this.propagationDelay = propagationDelay;
  }

  public static PinValue getValue(LogiPin logiPin, InstanceState instanceState)
  {
    Value value = instanceState.getPortValue(logiPin.index);
    if (value.isErrorValue())
    {
      return PinValue.Error;
    }
    else if (value == Value.TRUE)
    {
      instanceState.setPort(logiPin.index, Value.createUnknown(BitWidth.create(1)), logiPin.propagationDelay);
      return PinValue.High;
    }
    else if (value == Value.FALSE)
    {
      instanceState.setPort(logiPin.index, Value.createUnknown(BitWidth.create(1)), logiPin.propagationDelay);
      return PinValue.Low;
    }
    else
    {
      return PinValue.Unknown;
    }
  }

  public static void setValue(LogiPin logiPin, boolean value, InstanceState instanceState)
  {
    instanceState.setPort(logiPin.index,
                          Value.createKnown(BitWidth.create(1), value ? 1 : 0),
                          logiPin.propagationDelay);
  }
}

