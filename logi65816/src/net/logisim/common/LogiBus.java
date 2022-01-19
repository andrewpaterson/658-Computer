package net.logisim.common;

import com.cburch.logisim.data.BitWidth;
import com.cburch.logisim.data.Value;
import com.cburch.logisim.instance.InstanceState;
import net.common.BusValue;

public class LogiBus
    extends LogiPort
{
  public int index;
  public int width;
  public int propagationDelay;

  public LogiBus(int index, int width, int propagationDelay)
  {
    this.index = index;
    this.width = width;
    this.propagationDelay = propagationDelay;
  }

  public static BusValue getValue(LogiBus logiBus, InstanceState instanceState)
  {
    if (instanceState.isPortConnected(logiBus.index))
    {
      Value portValue = instanceState.getPortValue(logiBus.index);
      if (portValue.isErrorValue())
      {
        return BusValue.error();
      }
      else if (portValue.isUnknown())
      {
        return BusValue.unknown();
      }
      else
      {
        instanceState.setPort(logiBus.index, Value.createUnknown(BitWidth.create(logiBus.width)), logiBus.propagationDelay);
        return new BusValue(portValue.toLongValue());
      }
    }
    else
    {
      return BusValue.notConnected();
    }
  }

  public static BusValue getValueWithoutSet(LogiBus logiBus, InstanceState instanceState)
  {
    if (instanceState.isPortConnected(logiBus.index))
    {
      Value portValue = instanceState.getPortValue(logiBus.index);
      if (portValue.isErrorValue())
      {
        return BusValue.error();
      }
      else if (portValue.isUnknown())
      {
        return BusValue.unknown();
      }
      else
      {
        return new BusValue(portValue.toLongValue());
      }
    }
    else
    {
      return BusValue.notConnected();
    }
  }

  public static void setValue(LogiBus logiBus, long value, InstanceState instanceState)
  {
    instanceState.setPort(logiBus.index,
                          Value.createKnown(BitWidth.create(logiBus.width), value),
                          logiBus.propagationDelay);
  }
}

