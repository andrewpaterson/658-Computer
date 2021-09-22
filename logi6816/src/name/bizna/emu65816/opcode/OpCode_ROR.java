package name.bizna.emu65816.opcode;

import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Cpu65816;

public class OpCode_ROR
    extends OpCode
{
  public OpCode_ROR(String mName, byte mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  @Override
  public void execute(Cpu65816 cpu)
  {

  }

#define DO_ROR_8_BIT(value) {                                   \
  bool carryWasSet = mCpuStatus.carryFlag();                  \
  bool carryWillBeSet = (value & 0x01) != 0;                  \
  value = value >> 1;                                         \
  if (carryWasSet) Binary::setBitIn8BitValue(&value, 7);      \
    else Binary::clearBitIn8BitValue(&value, 7);                \
  mCpuStatus.setCarryFlag(carryWillBeSet);              \
  mCpuStatus.updateSignAndZeroFlagFrom8BitValue(value);       \
}

#define DO_ROR_16_BIT(value) {                                  \
  bool carryWasSet = mCpuStatus.carryFlag();                  \
  bool carryWillBeSet = (value & 0x0001) != 0;                \
  value = value >> 1;                                         \
  if (carryWasSet) Binary::setBitIn16BitValue(&value, 15);    \
    else Binary::clearBitIn16BitValue(&value, 15);              \
  mCpuStatus.setCarryFlag(carryWillBeSet);              \
  mCpuStatus.updateSignAndZeroFlagFrom16BitValue(value);      \
}

  /**
   * This file contains implementations for all ROR OpCodes.
   */
  void Cpu65816::executeMemoryROR(OpCode &opCode)
{
  Address opCodeDataAddress = getAddressOfOpCodeData(opCode);

  if(accumulatorIs8BitWide()) {
    uint8_t value = mSystemBus.readByte(opCodeDataAddress);
    DO_ROR_8_BIT(value);
    mSystemBus.storeByte(opCodeDataAddress, value);
  } else {
    uint16_t value = mSystemBus.readTwoBytes(opCodeDataAddress);
    DO_ROR_16_BIT(value);
    mSystemBus.storeTwoBytes(opCodeDataAddress, value);
  }
}

  void Cpu65816::executeAccumulatorROR(OpCode &opCode)
{
  if(accumulatorIs8BitWide()) {
    uint8_t value = Binary::lower8BitsOf(mA);
    DO_ROR_8_BIT(value);
    Binary::setLower8BitsOf16BitsValue(&mA, value);
  } else {
    uint16_t value = mA;
    DO_ROR_16_BIT(value);
    mA = value;
  }
}

  void Cpu65816::executeROR(OpCode &opCode)
{
  switch (opCode.getCode()) {
    case (0x6A):                // ROR accumulator
    {
      executeAccumulatorROR(opCode);
      addToProgramAddressAndCycles(1, 2);
      break;
    }
    case (0x6E):                // ROR #addr
    {
      executeMemoryROR(opCode);
      if (accumulatorIs8BitWide()) {
        addToProgramAddressAndCycles(3, 6);
      } else {
        addToProgramAddressAndCycles(3, 8);
      }
      break;
    }
    case (0x66):                // ROR Direct Page
    {
      executeMemoryROR(opCode);
      int opCycles = Binary::lower8BitsOf(mD) != 0 ? 1 : 0;
      if (accumulatorIs8BitWide()) {
        addToProgramAddressAndCycles(2, 5+opCycles);
      } else {
        addToProgramAddressAndCycles(2, 7+opCycles);
      }
      break;
    }
    case (0x7E):                // ROR Absolute Indexed, X
    {
      executeMemoryROR(opCode);
      short opCycles = 0;
      if (accumulatorIs8BitWide()) {
        addToProgramAddressAndCycles(3, 7+opCycles);
      } else {
        addToProgramAddressAndCycles(3, 9+opCycles);
      }
      break;
    }
    case (0x76):                // ROR Direct Page Indexed, X
    {
      executeMemoryROR(opCode);
      int opCycles = Binary::lower8BitsOf(mD) != 0 ? 1 : 0;
      if (accumulatorIs8BitWide()) {
        addToProgramAddressAndCycles(2, 6+opCycles);
      } else {
        addToProgramAddressAndCycles(2, 8+opCycles);
      }
      break;
    }
    default:
    {
      LOG_UNEXPECTED_OPCODE(opCode);
    }
  }
}
}
