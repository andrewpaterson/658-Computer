package name.bizna.emu65816;

public class CpuStatus
{
  public static final byte STATUS_CARRY = 0x01;
  public static final byte STATUS_ZERO = 0x02;
  public static final byte STATUS_INTERRUPT_DISABLE = 0x04;
  public static final byte STATUS_DECIMAL = 0x08;

  // In emulation mode
  public static final byte STATUS_BREAK = 0X10;
  // In native mode (x = 0, 16 bit)
  public static final byte STATUS_INDEX_WIDTH = 0X10;
  // Only used in native mode
  public static final byte STATUS_ACCUMULATOR_WIDTH = 0X20;

  public static final byte STATUS_OVERFLOW = 0X40;
  public static final byte STATUS_SIGN = (byte) 0X80;

  boolean mZeroFlag;
  boolean mSignFlag;
  boolean mDecimalFlag;
  boolean mInterruptDisableFlag;
  boolean mAccumulatorWidthFlag;
  boolean mIndexWidthFlag;
  boolean mCarryFlag;
  boolean mEmulationFlag = true; // CPU Starts in emulation mode
  boolean mOverflowFlag;
  boolean mBreakFlag;

  CpuStatus()
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

  void setZeroFlag()
  {
    mZeroFlag = true;
  }

  void setSignFlag()
  {
    mSignFlag = true;
  }

  void setDecimalFlag()
  {
    mDecimalFlag = true;
  }

  void setInterruptDisableFlag()
  {
    mInterruptDisableFlag = true;
  }

  void setAccumulatorWidthFlag()
  {
    mAccumulatorWidthFlag = true;
  }

  void setIndexWidthFlag()
  {
    mIndexWidthFlag = true;
  }

  void setCarryFlag(boolean carryFlag)
  {
    mCarryFlag = carryFlag;
  }

  void setEmulationFlag()
  {
    mEmulationFlag = true;
  }

  void clearZeroFlag()
  {
    mZeroFlag = false;
  }

  void clearSignFlag()
  {
    mSignFlag = false;
  }

  void clearDecimalFlag()
  {
    mDecimalFlag = false;
  }

  void clearInterruptDisableFlag()
  {
    mInterruptDisableFlag = false;
  }

  void clearAccumulatorWidthFlag()
  {
    mAccumulatorWidthFlag = false;
  }

  void clearIndexWidthFlag()
  {
    mIndexWidthFlag = false;
  }

  void clearEmulationFlag()
  {
    mEmulationFlag = false;
  }

  boolean zeroFlag()
  {
    return mZeroFlag;
  }

  boolean signFlag()
  {
    return mSignFlag;
  }

  boolean decimalFlag()
  {
    return mDecimalFlag;
  }

  boolean interruptDisableFlag()
  {
    return mInterruptDisableFlag;
  }

  boolean accumulatorWidthFlag()
  {
    return mAccumulatorWidthFlag;
  }

  boolean indexWidthFlag()
  {
    return mIndexWidthFlag;
  }

  boolean carryFlag()
  {
    return mCarryFlag;
  }

  boolean emulationFlag()
  {
    return mEmulationFlag;
  }

  void setBreakFlag()
  {
    mBreakFlag = true;
  }

  void clearBreakFlag()
  {
    mBreakFlag = false;
  }

  boolean breakFlag()
  {
    return mBreakFlag;
  }

  void setOverflowFlag()
  {
    mOverflowFlag = true;
  }

  void clearOverflowFlag()
  {
    mOverflowFlag = false;
  }

  boolean overflowFlag()
  {
    return mOverflowFlag;
  }

  byte getRegisterValue()
  {
    byte value = 0;
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

  void setRegisterValue(byte value)
  {
    setCarryFlag((value & STATUS_CARRY) != 0);

    if ((value & STATUS_ZERO) != 0)
    {
      setZeroFlag();
    }
    else
    {
      clearZeroFlag();
    }

    if ((value & STATUS_INTERRUPT_DISABLE) != 0)
    {
      setInterruptDisableFlag();
    }
    else
    {
      clearInterruptDisableFlag();
    }

    if ((value & STATUS_DECIMAL) != 0)
    {
      setDecimalFlag();
    }
    else
    {
      clearDecimalFlag();
    }

    if (emulationFlag())
    {
      if ((value & STATUS_BREAK) != 0)
      {
        setBreakFlag();
      }
      else
      {
        clearBreakFlag();
      }
    }
    else
    {
      if ((value & STATUS_INDEX_WIDTH) != 0)
      {
        setIndexWidthFlag();
      }
      else
      {
        clearIndexWidthFlag();
      }
    }

    if (!emulationFlag() && ((value & STATUS_ACCUMULATOR_WIDTH) != 0))
    {
      setAccumulatorWidthFlag();
    }
    else
    {
      clearAccumulatorWidthFlag();
    }

    if ((value & STATUS_OVERFLOW) != 0)
    {
      setOverflowFlag();
    }
    else
    {
      clearOverflowFlag();
    }

    if ((value & STATUS_SIGN) != 0)
    {
      setSignFlag();
    }
    else
    {
      clearSignFlag();
    }
  }

  void updateZeroFlagFrom8BitValue(byte value)
  {
    if (Binary.is8bitValueZero (value))
      setZeroFlag();
    else clearZeroFlag();
  }

  void updateZeroFlagFrom16BitValue(short value)
  {
    if (Binary.is16bitValueZero (value))setZeroFlag();
    else clearZeroFlag();
  }

  void updateSignFlagFrom8BitValue(byte value)
  {
    if (Binary.is8bitValueNegative (value))setSignFlag();
    else clearSignFlag();
  }

  void updateSignFlagFrom16BitValue(short value)
  {
    if (Binary.is16bitValueNegative (value))setSignFlag();
    else clearSignFlag();
  }

  void updateSignAndZeroFlagFrom8BitValue(byte value)
  {
    updateSignFlagFrom8BitValue(value);
    updateZeroFlagFrom8BitValue(value);
  }

  void updateSignAndZeroFlagFrom16BitValue(short value)
  {
    updateSignFlagFrom16BitValue(value);
    updateZeroFlagFrom16BitValue(value);
  }
}

