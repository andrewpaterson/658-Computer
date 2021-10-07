package name.bizna.cpu;

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

  public boolean isZeroFlag()
  {
    return zeroFlag;
  }

  public boolean isNegative()
  {
    return negativeFlag;
  }

  public boolean isDecimal()
  {
    return decimalFlag;
  }

  public boolean isInterruptDisable()
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

  public boolean isCarry()
  {
    return carryFlag;
  }

  public boolean isEmulation()
  {
    return emulationFlag;
  }

  public boolean isBreak()
  {
    return breakFlag;
  }

  public boolean isOverflowFlag()
  {
    return overflowFlag;
  }

  public void setBreakFlag(boolean breakFlag)
  {
    this.breakFlag = breakFlag;
  }

  public void setOverflowFlag(boolean overflowFlag)
  {
    this.overflowFlag = overflowFlag;
  }

  public int getRegisterValue()
  {
    int value = 0;
    if (isCarry())
    {
      value |= STATUS_CARRY;
    }
    if (isZeroFlag())
    {
      value |= STATUS_ZERO;
    }
    if (isInterruptDisable())
    {
      value |= STATUS_INTERRUPT_DISABLE;
    }
    if (isDecimal())
    {
      value |= STATUS_DECIMAL;
    }
    if (isEmulation() && isBreak())
    {
      value |= STATUS_BREAK;
    }
    if (!isEmulation() && isIndex8Bit())
    {
      value |= STATUS_INDEX_WIDTH;
    }
    if (!isEmulation() && isAccumulator8Bit())
    {
      value |= STATUS_ACCUMULATOR_WIDTH;
    }
    if (isOverflowFlag())
    {
      value |= STATUS_OVERFLOW;
    }
    if (isNegative())
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

    if (isEmulation())
    {
      setBreakFlag((value & STATUS_BREAK) != 0);
    }
    else
    {
      setIndexWidthFlag((value & STATUS_INDEX_WIDTH) != 0);
    }

    setAccumulatorWidthFlag(!isEmulation() && ((value & STATUS_ACCUMULATOR_WIDTH) != 0));
    setOverflowFlag((value & STATUS_OVERFLOW) != 0);

    setNegativeFlag((value & STATUS_NEGATIVE) != 0);
  }

  public void setZeroFlagFrom8BitValue(int value)
  {
    setZeroFlag(Cpu65816.is8bitValueZero(value));
  }

  public void setZeroFlagFrom16BitValue(int value)
  {
    setZeroFlag(Cpu65816.is16bitValueZero(value));
  }

  public void setNegativeFlagFrom8BitValue(int value)
  {
    setNegativeFlag(Cpu65816.is8bitValueNegative(value));
  }

  public void setNegativeFlagFrom16BitValue(int value)
  {
    setNegativeFlag(Cpu65816.is16bitValueNegative(value));
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

