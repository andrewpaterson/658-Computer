package name.bizna.emu65816;

public class CpuStatus
{
  public static final int STATUS_CARRY = 0x01;
  public static final int STATUS_ZERO = 0x02;
  public static final int STATUS_INTERRUPT_DISABLE = 0x04;
  public static final int STATUS_DECIMAL = 0x08;

  // In emulation mode
  public static final int STATUS_BREAK = 0X10;
  // In native mode (x = 0, 16 bit)
  public static final int STATUS_INDEX_WIDTH = 0X10;
  // Only used in native mode
  public static final int STATUS_ACCUMULATOR_WIDTH = 0X20;

  public static final int STATUS_OVERFLOW = 0X40;
  public static final int STATUS_SIGN = 0X80;

  boolean mZeroFlag;
  boolean mSignFlag;
  boolean mDecimalFlag;
  boolean mInterruptDisableFlag;
  boolean mAccumulatorWidthFlag;
  boolean mIndexWidthFlag;
  boolean mCarryFlag;
  boolean mEmulationFlag;
  boolean mOverflowFlag;
  boolean mBreakFlag;

  public CpuStatus()
  {
    mZeroFlag = false;
    mSignFlag = false;
    mDecimalFlag = false;
    mInterruptDisableFlag = false;
    mAccumulatorWidthFlag = false;
    mIndexWidthFlag = false;
    mCarryFlag = false;
    mEmulationFlag = true; // CPU Starts in emulation mode
    mOverflowFlag = false;
    mBreakFlag = false;
  }

  public void setZeroFlag(boolean zeroFlag)
  {
    mZeroFlag = zeroFlag;
  }

  public void setSignFlag(boolean signFlag)
  {
    mSignFlag = signFlag;
  }

  public void setDecimalFlag(boolean decimalFlag)
  {
    mDecimalFlag = decimalFlag;
  }

  public void setInterruptDisableFlag(boolean interruptDisableFlag)
  {
    mInterruptDisableFlag = interruptDisableFlag;
  }

  public void setAccumulatorWidthFlag(boolean accumulatorWidthFlag)
  {
    mAccumulatorWidthFlag = accumulatorWidthFlag;
  }

  public void setIndexWidthFlag(boolean indexWidthFlag)
  {
    mIndexWidthFlag = indexWidthFlag;
  }

  public void setCarryFlag(boolean carryFlag)
  {
    mCarryFlag = carryFlag;
  }

  public void setEmulationFlag(boolean emulationFlag)
  {
    mEmulationFlag = emulationFlag;
  }

  public boolean zeroFlag()
  {
    return mZeroFlag;
  }

  public boolean signFlag()
  {
    return mSignFlag;
  }

  public boolean decimalFlag()
  {
    return mDecimalFlag;
  }

  public boolean interruptDisableFlag()
  {
    return mInterruptDisableFlag;
  }

  public boolean accumulatorWidthFlag()
  {
    return mAccumulatorWidthFlag;
  }

  public boolean indexWidthFlag()
  {
    return mIndexWidthFlag;
  }

  public boolean carryFlag()
  {
    return mCarryFlag;
  }

  public boolean emulationFlag()
  {
    return mEmulationFlag;
  }

  public void setBreakFlag(boolean breakFlag)
  {
    mBreakFlag = breakFlag;
  }

  public boolean breakFlag()
  {
    return mBreakFlag;
  }

  public void setOverflowFlag(boolean overflowFlag)
  {
    mOverflowFlag = overflowFlag;
  }

  public boolean overflowFlag()
  {
    return mOverflowFlag;
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
    if (emulationFlag() && breakFlag())
    {
      value |= STATUS_BREAK;
    }
    if (!emulationFlag() && indexWidthFlag())
    {
      value |= STATUS_INDEX_WIDTH;
    }
    if (!emulationFlag() && accumulatorWidthFlag())
    {
      value |= STATUS_ACCUMULATOR_WIDTH;
    }
    if (overflowFlag())
    {
      value |= STATUS_OVERFLOW;
    }
    if (signFlag())
    {
      value |= STATUS_SIGN;
    }

    return value;
  }

  public void setRegisterValue(int value)
  {
    setCarryFlag((value & STATUS_CARRY) != 0);
    setZeroFlag((value & STATUS_ZERO) != 0);
    setInterruptDisableFlag((value & STATUS_INTERRUPT_DISABLE) != 0);
    setDecimalFlag((value & STATUS_DECIMAL) != 0);

    if (emulationFlag())
    {
      setBreakFlag((value & STATUS_BREAK) != 0);
    }
    else
    {
      setIndexWidthFlag((value & STATUS_INDEX_WIDTH) != 0);
    }

    setAccumulatorWidthFlag(!emulationFlag() && ((value & STATUS_ACCUMULATOR_WIDTH) != 0));
    setOverflowFlag((value & STATUS_OVERFLOW) != 0);

    setSignFlag((value & STATUS_SIGN) != 0);
  }

  public void updateZeroFlagFrom8BitValue(int value)
  {
    setZeroFlag(Binary.is8bitValueZero(value));
  }

  public void updateZeroFlagFrom16BitValue(int value)
  {
    setZeroFlag(Binary.is16bitValueZero(value));
  }

  public void updateSignFlagFrom8BitValue(int value)
  {
    setSignFlag(Binary.is8bitValueNegative(value));
  }

  public void updateSignFlagFrom16BitValue(int value)
  {
    setSignFlag(Binary.is16bitValueNegative(value));
  }

  public void updateSignAndZeroFlagFrom8BitValue(int value)
  {
    updateSignFlagFrom8BitValue(value);
    updateZeroFlagFrom8BitValue(value);
  }

  public void updateSignAndZeroFlagFrom16BitValue(int value)
  {
    updateSignFlagFrom16BitValue(value);
    updateZeroFlagFrom16BitValue(value);
  }
}

