package name.bizna.emu65816;

public class CpuStatus
{
  public static final int STATUS_CARRY = 0x01;
  public static final int STATUS_ZERO = 0x02;
  public static final int STATUS_INTERRUPT_DISABLE = 0x04;
  public static final int STATUS_DECIMAL = 0x08;

  public static final int STATUS_BREAK = 0X10; // In emulation mode
  public static final int STATUS_INDEX_WIDTH = 0X10; // In native mode (x = 0, 16 bit)
  public static final int STATUS_ACCUMULATOR_WIDTH = 0X20;

  public static final int STATUS_OVERFLOW = 0X40;
  public static final int STATUS_NEGATIVE = 0X80;

  boolean zeroFlag;
  boolean negativeFlag;
  boolean decimalFlag;
  boolean interruptDisableFlag;
  boolean accumulatorWidthFlag;
  boolean mIndexWidthFlag;
  boolean carryFlag;
  boolean emulationFlag;
  boolean overflowFlag;
  boolean breakFlag;

  public CpuStatus()
  {
    zeroFlag = false;
    negativeFlag = false;
    decimalFlag = false;
    interruptDisableFlag = false;
    accumulatorWidthFlag = false;
    mIndexWidthFlag = false;
    carryFlag = false;
    emulationFlag = true; // CPU Starts in emulation mode
    overflowFlag = false;
    breakFlag = false;
  }

  public void setZeroFlag(boolean zeroFlag)
  {
    this.zeroFlag = zeroFlag;
  }

  public void setNegativeFlag(boolean signFlag)
  {
    negativeFlag = signFlag;
  }

  public void setDecimalFlag(boolean decimalFlag)
  {
    this.decimalFlag = decimalFlag;
  }

  public void setInterruptDisableFlag(boolean interruptDisableFlag)
  {
    this.interruptDisableFlag = interruptDisableFlag;
  }

  public void setAccumulatorWidthFlag(boolean accumulatorWidthFlag)
  {
    this.accumulatorWidthFlag = accumulatorWidthFlag;
  }

  public void setIndexWidthFlag(boolean indexWidthFlag)
  {
    mIndexWidthFlag = indexWidthFlag;
  }

  public void setCarryFlag(boolean carryFlag)
  {
    this.carryFlag = carryFlag;
  }

  public void setEmulationFlag(boolean emulationFlag)
  {
    this.emulationFlag = emulationFlag;
  }

  public boolean zeroFlag()
  {
    return zeroFlag;
  }

  public boolean negativeFlag()
  {
    return negativeFlag;
  }

  public boolean decimalFlag()
  {
    return decimalFlag;
  }

  public boolean interruptDisableFlag()
  {
    return interruptDisableFlag;
  }

  public boolean isAccumulator8Bit()
  {
    return accumulatorWidthFlag;
  }

  public boolean isIndex8Bit()
  {
    return mIndexWidthFlag;
  }

  public boolean carryFlag()
  {
    return carryFlag;
  }

  public boolean isEmulationMode()
  {
    return emulationFlag;
  }

  public void setBreakFlag(boolean breakFlag)
  {
    this.breakFlag = breakFlag;
  }

  public boolean breakFlag()
  {
    return breakFlag;
  }

  public void setOverflowFlag(boolean overflowFlag)
  {
    this.overflowFlag = overflowFlag;
  }

  public boolean overflowFlag()
  {
    return overflowFlag;
  }

  public int getRegisterValue()
  {
    int value = 0;
    if (carryFlag())
    {
      value |= STATUS_CARRY;
    }
    if (zeroFlag())
    {
      value |= STATUS_ZERO;
    }
    if (interruptDisableFlag())
    {
      value |= STATUS_INTERRUPT_DISABLE;
    }
    if (decimalFlag())
    {
      value |= STATUS_DECIMAL;
    }
    if (isEmulationMode() && breakFlag())
    {
      value |= STATUS_BREAK;
    }
    if (!isEmulationMode() && isIndex8Bit())
    {
      value |= STATUS_INDEX_WIDTH;
    }
    if (!isEmulationMode() && isAccumulator8Bit())
    {
      value |= STATUS_ACCUMULATOR_WIDTH;
    }
    if (overflowFlag())
    {
      value |= STATUS_OVERFLOW;
    }
    if (negativeFlag())
    {
      value |= STATUS_NEGATIVE;
    }

    return value;
  }

  public void setRegisterValue(int value)
  {
    setCarryFlag((value & STATUS_CARRY) != 0);
    setZeroFlag((value & STATUS_ZERO) != 0);
    setInterruptDisableFlag((value & STATUS_INTERRUPT_DISABLE) != 0);
    setDecimalFlag((value & STATUS_DECIMAL) != 0);

    if (isEmulationMode())
    {
      setBreakFlag((value & STATUS_BREAK) != 0);
    }
    else
    {
      setIndexWidthFlag((value & STATUS_INDEX_WIDTH) != 0);
    }

    setAccumulatorWidthFlag(!isEmulationMode() && ((value & STATUS_ACCUMULATOR_WIDTH) != 0));
    setOverflowFlag((value & STATUS_OVERFLOW) != 0);

    setNegativeFlag((value & STATUS_NEGATIVE) != 0);
  }

  public void setZeroFlagFrom8BitValue(int value)
  {
    setZeroFlag(Binary.is8bitValueZero(value));
  }

  public void setZeroFlagFrom16BitValue(int value)
  {
    setZeroFlag(Binary.is16bitValueZero(value));
  }

  public void setNegativeFlagFrom8BitValue(int value)
  {
    setNegativeFlag(Binary.is8bitValueNegative(value));
  }

  public void setNegativeFlagFrom16BitValue(int value)
  {
    setNegativeFlag(Binary.is16bitValueNegative(value));
  }

  public void setSignAndZeroFlagFrom8BitValue(int value)
  {
    setNegativeFlagFrom8BitValue(value);
    setZeroFlagFrom8BitValue(value);
  }

  public void setSignAndZeroFlagFrom16BitValue(int value)
  {
    setNegativeFlagFrom16BitValue(value);
    setZeroFlagFrom16BitValue(value);
  }
}

