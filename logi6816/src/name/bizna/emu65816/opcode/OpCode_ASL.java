package name.bizna.emu65816.opcode;

import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Cpu65816;

public class OpCode_ASL
    extends OpCode
{
  public OpCode_ASL(String mName, byte mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  @Override
  public void execute(Cpu65816 cpu)
  {

  }

#define DO_ASL_8_BIT(value) {                                   \
  bool newCarry = value & 0x80;                               \
  value = value << 1;                                         \
  mCpuStatus.setCarryFlag(newCarry);                    \
  mCpuStatus.updateSignAndZeroFlagFrom8BitValue(value);       \
}

  /**
   * Arithmetic Shift Left
   *
   * This file contains implementations for all ASL OpCodes.
   */

  void Cpu65816::executeMemoryASL(OpCode &opCode)
{
  Address opCodeDataAddress = getAddressOfOpCodeData(opCode);

  if(accumulatorIs8BitWide())
  {
    uint8_t value = mSystemBus.readByte(opCodeDataAddress);
    bool newCarry = value & 0x80;
    value = value << 1;
    mCpuStatus.setCarryFlag(newCarry);
    mCpuStatus.updateSignAndZeroFlagFrom8BitValue(value);
    mSystemBus.storeByte(opCodeDataAddress, value);
  }
  else
  {
    uint16_t value = mSystemBus.readTwoBytes(opCodeDataAddress);
    bool newCarry = value & 0x8000;
    value = value << 1;
    mCpuStatus.setCarryFlag(newCarry);
    mCpuStatus.updateSignAndZeroFlagFrom16BitValue(value);
    mSystemBus.storeTwoBytes(opCodeDataAddress, value);
  }
}

  void Cpu65816::executeAccumulatorASL(OpCode &opCode)
{
  if(accumulatorIs8BitWide())
  {
    uint8_t value = Binary::lower8BitsOf(mA);
    DO_ASL_8_BIT(value);
    Binary::setLower8BitsOf16BitsValue(&mA, value);
  }
  else
  {
    bool newCarry = mA & 0x8000;
    mA = mA << 1;
    mCpuStatus.setCarryFlag(newCarry);
    mCpuStatus.updateSignAndZeroFlagFrom16BitValue(mA);
  }
}

  void Cpu65816::executeASL(OpCode &opCode)
{
  switch (opCode.getCode()) {
    case (0x0A):                // ASL Accumulator
    {
      executeAccumulatorASL(opCode);
      addToProgramAddressAndCycles(1, 2);
      break;
    }
    case (0x0E):                // ASL Absolute
    {
      if (accumulatorIs16BitWide()) {
        addToCycles(2);
      }

      executeMemoryASL(opCode);
      addToProgramAddressAndCycles(3, 6);
      break;
    }
    case (0x06):                // ASL Direct Page
    {
      if (accumulatorIs16BitWide()) {
        addToCycles(2);
      }
      if (Binary::lower8BitsOf(mD) != 0) {
      addToCycles(1);
    }

      executeMemoryASL(opCode);
      addToProgramAddressAndCycles(2, 5);
      break;
    }
    case (0x1E):                // ASL Absolute Indexed, X
    {
      if (accumulatorIs16BitWide()) {
        addToCycles(2);
      }

      executeMemoryASL(opCode);
      addToProgramAddressAndCycles(3, 7);
      break;
    }
    case (0x16):                // ASL Direct Page Indexed, X
    {
      if (accumulatorIs16BitWide()) {
        addToCycles(2);
      }
      if (Binary::lower8BitsOf(mD) != 0) {
      addToCycles(1);
    }

      executeMemoryASL(opCode);
      addToProgramAddressAndCycles(2, 6);
      break;
    }
    default:
    {
      LOG_UNEXPECTED_OPCODE(opCode);
    }
  }
}
}
