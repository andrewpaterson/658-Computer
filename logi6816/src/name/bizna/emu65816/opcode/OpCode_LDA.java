package name.bizna.emu65816.opcode;

import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Cpu65816;

public class OpCode_LDA
    extends OpCode
{
  public OpCode_LDA(String mName, byte mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  @Override
  public void execute(Cpu65816 cpu)
  {

  }

  void Cpu65816::executeLDA8Bit(OpCode &opCode)
{
  Address opCodeDataAddress = getAddressOfOpCodeData(opCode);
  uint8_t value = mSystemBus.readByte(opCodeDataAddress);
  Binary::setLower8BitsOf16BitsValue(&mA, value);
  mCpuStatus.updateSignAndZeroFlagFrom8BitValue(value);
}

  void Cpu65816::executeLDA16Bit(OpCode &opCode)
{
  Address opCodeDataAddress = getAddressOfOpCodeData(opCode);
  mA = mSystemBus.readTwoBytes(opCodeDataAddress);
  mCpuStatus.updateSignAndZeroFlagFrom16BitValue(mA);
}

  void Cpu65816::executeLDA(OpCode &opCode)
{
  if (accumulatorIs16BitWide())
  {
    executeLDA16Bit(opCode);
    addToCycles(1);
  }
  else
  {
    executeLDA8Bit(opCode);
  }

  switch (opCode.getCode()) {
    case (0xA9):                // LDA Immediate
    {
      if (accumulatorIs16BitWide()) {
        addToProgramAddress(1);
      }
      addToProgramAddressAndCycles(2, 2);
      break;
    }
    case (0xAD):                // LDA Absolute
    {
      addToProgramAddressAndCycles(3, 4);
      break;
    }
    case (0xAF):                // LDA Absolute Long
    {
      addToProgramAddressAndCycles(4, 5);
      break;
    }
    case (0xA5):                // LDA Direct Page
    {
      if (Binary::lower8BitsOf(mD) != 0) {
      addToCycles(1);
    }
      addToProgramAddressAndCycles(2, 3);
      break;
    }
    case (0xB2):                // LDA Direct Page Indirect
    {
      if (Binary::lower8BitsOf(mD) != 0) {
      addToCycles(1);
    }
      addToProgramAddressAndCycles(2, 5);
      break;
    }
    case (0xA7):                // LDA Direct Page Indirect Long
    {
      if (Binary::lower8BitsOf(mD) != 0) {
      addToCycles(1);
    }
      addToProgramAddressAndCycles(2, 6);
      break;
    }
    case (0xBD):                // LDA Absolute Indexed, X
    {
      if (opCodeAddressingCrossesPageBoundary(opCode)) {
        addToCycles(1);
      }
      addToProgramAddressAndCycles(3, 4);
      break;
    }
    case (0xBF):                // LDA Absolute Long Indexed, X
    {
      addToProgramAddressAndCycles(4, 5);
      break;
    }
    case (0xB9):                // LDA Absolute Indexed, Y
    {
      if (opCodeAddressingCrossesPageBoundary(opCode)) {
        addToCycles(1);
      }
      addToProgramAddressAndCycles(3, 4);
      break;
    }
    case (0xB5):                // LDA Direct Page Indexed, X
    {
      if (Binary::lower8BitsOf(mD) != 0) {
      addToCycles(1);
    }
      addToProgramAddressAndCycles(2, 4);
      break;
    }
    case (0xA1):                // LDA Direct Page Indexed Indirect, X
    {
      if (Binary::lower8BitsOf(mD) != 0) {
      addToCycles(1);
    }
      addToProgramAddressAndCycles(2, 6);
      break;
    }
    case (0xB1):                // LDA Direct Page Indirect Indexed, Y
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
    case (0xB7):                // LDA Direct Page DP Indirect Long Indexed, Y
    {
      if (Binary::lower8BitsOf(mD) != 0) {
      addToCycles(1);
    }
      addToProgramAddressAndCycles(2, 6);
      break;
    }
    case (0xA3):                // LDA Stack Relative
    {
      addToProgramAddressAndCycles(2, 4);
      break;
    }
    case (0xB3):                // LDA Stack Relative Indirect Indexed, Y
    {
      addToProgramAddressAndCycles(2, 7);
      break;
    }
    default:
    {
      LOG_UNEXPECTED_OPCODE(opCode);
    }
  }
}
}
