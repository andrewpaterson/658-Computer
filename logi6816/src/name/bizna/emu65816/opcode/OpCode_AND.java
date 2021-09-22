package name.bizna.emu65816.opcode;

import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Cpu65816;

public class OpCode_AND
    extends OpCode
{
  public OpCode_AND(String mName, byte mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  @Override
  public void execute(Cpu65816 cpu)
  {

  }

  void Cpu65816::executeAND8Bit(OpCode &opCode)
{
  Address opCodeDataAddress = getAddressOfOpCodeData(opCode);
  uint8_t operand = mSystemBus.readByte(opCodeDataAddress);
  uint8_t result = Binary::lower8BitsOf(mA) & operand;
  mCpuStatus.updateSignAndZeroFlagFrom8BitValue(result);
  Binary::setLower8BitsOf16BitsValue(&mA, result);
}

  void Cpu65816::executeAND16Bit(OpCode &opCode)
{
  Address opCodeDataAddress = getAddressOfOpCodeData(opCode);
  uint16_t operand = mSystemBus.readTwoBytes(opCodeDataAddress);
  uint16_t result = mA & operand;
  mCpuStatus.updateSignAndZeroFlagFrom16BitValue(result);
  mA = result;
}

  void Cpu65816::executeAND(OpCode &opCode)
{
  if (accumulatorIs16BitWide())
  {
    executeAND16Bit(opCode);
    addToCycles(1);
  }
  else
  {
    executeAND8Bit(opCode);
  }

  switch (opCode.getCode()) {
    case (0x29):                // AND Immediate
    {
      if (accumulatorIs16BitWide()) {
        addToProgramAddress(1);
      }
      addToProgramAddressAndCycles(2, 2);
      break;
    }
    case (0x2D):                // AND Absolute
    {
      addToProgramAddressAndCycles(3, 4);
      break;
    }
    case (0x2F):                // AND Absolute Long
    {
      addToProgramAddressAndCycles(4, 5);
      break;
    }
    case (0x25):                // AND Direct Page
    {
      if (Binary::lower8BitsOf(mD) != 0) {
      addToCycles(1);
    }
      addToProgramAddressAndCycles(2, 3);
      break;
    }
    case (0x32):                // AND Direct Page Indirect
    {
      if (Binary::lower8BitsOf(mD) != 0) {
      addToCycles(1);
    }
      addToProgramAddressAndCycles(2, 5);
      break;
    }
    case (0x27):                // AND Direct Page Indirect Long
    {
      if (Binary::lower8BitsOf(mD) != 0) {
      addToCycles(1);
    }
      addToProgramAddressAndCycles(2, 6);
      break;
    }
    case (0x3D):                // AND Absolute Indexed, X
    {
      if (opCodeAddressingCrossesPageBoundary(opCode)) {
        addToCycles(1);
      }
      addToProgramAddressAndCycles(3, 4);
      break;
    }
    case (0x3F):                // AND Absolute Long Indexed, X
    {
      addToProgramAddressAndCycles(4, 5);
      break;
    }
    case (0x39):                // AND Absolute Indexed, Y
    {
      if (opCodeAddressingCrossesPageBoundary(opCode)) {
        addToCycles(1);
      }
      addToProgramAddressAndCycles(3, 4);
      break;
    }
    case (0x35):                // AND Direct Page Indexed, X
    {
      if (Binary::lower8BitsOf(mD) != 0) {
      addToCycles(1);
    }
      addToProgramAddressAndCycles(2, 4);
      break;
    }
    case (0x21):                // AND Direct Page Indexed Indirect, X
    {
      if (Binary::lower8BitsOf(mD) != 0) {
      addToCycles(1);
    }
      addToProgramAddressAndCycles(2, 6);
      break;
    }
    case (0x31):                // AND Direct Page Indirect Indexed, Y
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
    case (0x37):                // AND Direct Page Indirect Long Indexed, Y
    {
      // TODO: The manual reports a '0' not on the cycles count for this OpCode.
      // No idea what that means.
      if (Binary::lower8BitsOf(mD) != 0) {
      addToCycles(1);
    }
      addToProgramAddressAndCycles(2, 6);
      break;
    }
    case (0x23):                // AND Stack Relative
    {
      addToProgramAddressAndCycles(2, 4);
      break;
    }
    case (0x33):                // AND Stack Relative Indirect Indexed, Y
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
