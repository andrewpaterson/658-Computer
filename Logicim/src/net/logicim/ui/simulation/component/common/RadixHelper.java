package net.logicim.ui.simulation.component.common;

import net.common.SimulatorException;
import net.common.util.StringUtil;
import net.logicim.data.common.Radix;
import net.logicim.domain.common.wire.TraceValue;

import static net.logicim.domain.common.wire.TraceValue.*;

public class RadixHelper
{
  public static int calculateMaxDigits(Radix radix, int bitWidth)
  {
    int maxDigits = 0;
    if (radix == Radix.BINARY)
    {
      maxDigits = bitWidth;
    }
    else if (radix == Radix.HEXADECIMAL)
    {
      maxDigits = (int) Math.ceil(bitWidth / 4.0f);
    }
    else if (radix == Radix.DECIMAL)
    {
      if (bitWidth < 63)
      {
        long maxInt = 1 << bitWidth - 1;
        String s = Long.toString(maxInt);
        maxDigits = s.length();
      }
      else if (bitWidth == 64)
      {
        maxDigits = 20;
      }
      else if (bitWidth == 63)
      {
        maxDigits = 19;
      }
      else
      {
        throw new SimulatorException("Cannot calculate maximum decimal digits for bitwidth [%s].", bitWidth);
      }
    }
    if (maxDigits < 1)
    {
      maxDigits = 1;
    }
    return maxDigits;
  }

  public static String getStringValue(TraceValue[] values, Radix radix, int maxDigits)
  {
    if (values == null)
    {
      return "";
    }

    if (radix == Radix.BINARY)
    {
      return toBinaryString(values);
    }
    else if (radix == Radix.HEXADECIMAL)
    {
      return toHexadecimalString(values);
    }
    else if (radix == Radix.DECIMAL)
    {
      return toDecimalString(values, maxDigits);
    }
    else
    {
      return "";
    }
  }

  private static String toDecimalString(TraceValue[] values, int maxDigits)
  {
    long longValue = 0;
    for (int i = 0; i < values.length; i++)
    {
      TraceValue value = values[i];
      if (value == High)
      {
        longValue += 1 << i;
      }
      else if (value == Undriven)
      {
        return StringUtil.pad(maxDigits, Character.toString(getBinaryChar(Undriven)));
      }
      else if (value == Unsettled)
      {
        return StringUtil.pad(maxDigits, Character.toString(getBinaryChar(Unsettled)));
      }
    }

    return Long.toString(longValue);
  }

  private static String toHexadecimalString(TraceValue[] values)
  {
    StringBuilder builder = new StringBuilder();
    int nybbleIndex = 0;
    boolean undriven = false;
    boolean unsettled = false;
    int nybble = 0;
    for (TraceValue value : values)
    {
      if (nybbleIndex == 0)
      {
        nybble = 0;
      }
      if (value == High)
      {
        nybble += 1 << nybbleIndex;
      }
      else if (value == Undriven)
      {
        undriven = true;
      }
      else if (value == Unsettled)
      {
        unsettled = true;
      }

      nybbleIndex++;
      if (nybbleIndex == 4)
      {
        nybbleIndex = 0;
        if (unsettled)
        {
          builder.append(getBinaryChar(Unsettled));
        }
        else if (undriven)
        {
          builder.append(getBinaryChar(Undriven));
        }
        else
        {
          builder.append(Integer.toHexString(nybble));
        }
        undriven = false;
        unsettled = false;
      }
    }
    builder.reverse();
    return builder.toString().toUpperCase();
  }

  private static String toBinaryString(TraceValue[] values)
  {
    StringBuilder builder = new StringBuilder();
    for (int i = values.length - 1; i >= 0; i--)
    {
      builder.append(getBinaryChar(values[i]));
    }
    return builder.toString();
  }

  private static char getBinaryChar(TraceValue value)
  {
    if (value == High)
    {
      return '1';
    }
    else if (value == Low)
    {
      return '0';
    }
    else if (value == Unsettled)
    {
      return 'X';
    }
    else if (value == Undriven)
    {
      return '.';
    }
    else
    {
      throw new SimulatorException("Cannot get binary value for unknown radix.");
    }
  }
}
