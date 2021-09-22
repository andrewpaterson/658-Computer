package name.bizna.emu65816.opcode;

import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Cpu65816;

public class OpCode_CMP
    extends OpCode
{
  public OpCode_CMP(String mName, byte mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  @Override
  public void execute(Cpu65816 cpu)
  {

  }

  void Cpu65816::execute8BitCMP(OpCode &opCode)
{
  Address valueAddress = getAddressOfOpCodeData(opCode);
  uint8_t value = mSystemBus.readByte(valueAddress);
  uint8_t result = Binary::lower8BitsOf(mA) - value;
  mCpuStatus.updateSignAndZeroFlagFrom8BitValue(result);
  bool carry = Binary::lower8BitsOf(mA) >= value;
  mCpuStatus.setCarryFlag(carry);
}

  void Cpu65816::execute16BitCMP(OpCode &opCode)
{
  Address valueAddress = getAddressOfOpCodeData(opCode);
  uint16_t value = mSystemBus.readTwoBytes(valueAddress);
  uint16_t result = mA - value;
  mCpuStatus.updateSignAndZeroFlagFrom16BitValue(result);
  bool carry = mA >= value;
  mCpuStatus.setCarryFlag(carry);
}
  void Cpu65816::executeCMP(OpCode &opCode)
{
  if (accumulatorIs8BitWide())
  {
    execute8BitCMP(opCode);
  }
  else
  {
    execute16BitCMP(opCode);
    addToCycles(1);
  }

  switch(opCode.getCode()) {
    case(0xC9):  // CMP Immediate
    {
      if (accumulatorIs16BitWide()) {
        addToProgramAddress(1);
      }
      addToProgramAddressAndCycles(2, 2);
      break;
    }
    case(0xCD):  // CMP Absolute
    {
      addToProgramAddressAndCycles(3, 4);
      break;
    }
    case(0xCF):  // CMP Absolute Long
    {
      addToProgramAddressAndCycles(4, 5);
      break;
    }
    case(0xC5):  // CMP Direct Page
    {
      if (Binary::lower8BitsOf(mD) != 0) {
      addToCycles(1);
    }
      addToProgramAddressAndCycles(2, 3);
      break;
    }
    case(0xD2):  // CMP Direct Page Indirect
    {
      if (Binary::lower8BitsOf(mD) != 0) {
      addToCycles(1);
    }
      addToProgramAddressAndCycles(2, 5);
      break;
    }
    case(0xC7):  // CMP Direct Page Indirect Long
    {
      addToProgramAddressAndCycles(2, 6);
      break;
    }
    case(0xDD):  // CMP Absolute Indexed, X
    {
      if (opCodeAddressingCrossesPageBoundary(opCode)) {
        addToCycles(1);
      }
      addToProgramAddressAndCycles(3, 4);
      break;
    }
    case(0xDF):  // CMP Absolute Long Indexed, X
    {
      addToProgramAddressAndCycles(4, 5);
      break;
    }
    case(0xD9):  // CMP Absolute Indexed, Y
    {
      if (opCodeAddressingCrossesPageBoundary(opCode)) {
        addToCycles(1);
      }
      addToProgramAddressAndCycles(3, 4);
      break;
    }
    case(0xD5):  // CMP Direct Page Indexed, X
    {
      if (Binary::lower8BitsOf(mD) != 0) {
      addToCycles(1);
    }
      addToProgramAddressAndCycles(2, 4);
      break;
    }
    case(0xC1):  // CMP Direct Page Indexed Indirect, X
    {
      if (Binary::lower8BitsOf(mD) != 0) {
      addToCycles(1);
    }
      addToProgramAddressAndCycles(2, 6);
      break;
    }
    case(0xD1):  // CMP Direct Page Indexed Indirect, Y
    {
      if (Binary::lower8BitsOf(mD) != 0) {
      addToCycles(1);
    }
      if (opCodeAddressingCrossesPageBoundary(opCode)) {
        addToCycles(1);
      }
      addToProgramAddressAndCycles(2, 5);
      break;
    }
    case(0xD7):  // CMP Direct Page Indirect Long Indexed, Y
    {
      if (Binary::lower8BitsOf(mD) != 0) {
      addToCycles(1);
    }
      addToProgramAddressAndCycles(2, 6);
      break;
    }
    case(0xC3):  // CMP Stack Relative
    {
      addToProgramAddressAndCycles(2, 4);
      break;
    }
    case(0xD3):  // CMP Stack Relative Indirect Indexed, Y
    {
      addToProgramAddressAndCycles(2, 7);
      break;
    }
    default: {
      LOG_UNEXPECTED_OPCODE(opCode);
    }
  }
}
}
