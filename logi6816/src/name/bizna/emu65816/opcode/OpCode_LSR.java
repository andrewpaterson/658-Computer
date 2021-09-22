package name.bizna.emu65816.opcode;

import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Cpu65816;

public class OpCode_LSR
    extends OpCode
{
  public OpCode_LSR(String mName, byte mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  @Override
  public void execute(Cpu65816 cpu)
  {

  }

  void Cpu65816::executeMemoryLSR(OpCode &opCode)
{
  Address opCodeDataAddress = getAddressOfOpCodeData(opCode);

  if(accumulatorIs8BitWide()) {
    uint8_t value = mSystemBus.readByte(opCodeDataAddress);
    DO_LSR_8_BIT(value);
    mSystemBus.storeByte(opCodeDataAddress, value);
  } else {
    uint16_t value = mSystemBus.readTwoBytes(opCodeDataAddress);
    DO_LSR_16_BIT(value);
    mSystemBus.storeTwoBytes(opCodeDataAddress, value);
  }
}

  void Cpu65816::executeAccumulatorLSR(OpCode &opCode)
{
  if(accumulatorIs8BitWide()) {
    uint8_t value = Binary::lower8BitsOf(mA);
    DO_LSR_8_BIT(value);
    Binary::setLower8BitsOf16BitsValue(&mA, value);
  } else {
    DO_LSR_16_BIT(mA);
  }
}

  void Cpu65816::executeLSR(OpCode &opCode)
{
  switch (opCode.getCode()) {
    case (0x4A):                // LSR Accumulator
    {
      executeAccumulatorLSR(opCode);
      addToProgramAddressAndCycles(1, 2);
      break;
    }
    case (0x4E):                // LSR Absolute
    {
      executeMemoryLSR(opCode);
      if (accumulatorIs16BitWide()) {
        addToCycles(2);
      }
      addToProgramAddressAndCycles(3, 6);
      break;
    }
    case (0x46):                // LSR Direct Page
    {
      executeMemoryLSR(opCode);
      if (accumulatorIs16BitWide()) {
        addToCycles(2);
      }
      if (Binary::lower8BitsOf(mD) != 0) {
      addToCycles(1);
    }

      addToProgramAddressAndCycles(2, 5);
      break;
    }
    case (0x5E):                // LSR Absolute Indexed, X
    {
      executeMemoryLSR(opCode);
      if (accumulatorIs16BitWide()) {
        addToCycles(2);
      }

      addToProgramAddressAndCycles(3, 7);
      break;
    }
    case (0x56):                // LSR Direct Page Indexed, X
    {
      executeMemoryLSR(opCode);
      if (accumulatorIs16BitWide()) {
        addToCycles(2);
      }
      if (Binary::lower8BitsOf(mD) != 0) {
      addToCycles(1);
    }

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
