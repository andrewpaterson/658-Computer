package name.bizna.emu65816.opcode;

import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Cpu65816;

public class OpCode_LDX
    extends OpCode
{
  public OpCode_LDX(String mName, byte mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  @Override
  public void execute(Cpu65816 cpu)
  {

  }

  void Cpu65816::executeLDX8Bit(OpCode &opCode)
{
  Address opCodeDataAddress = getAddressOfOpCodeData(opCode);
  uint8_t value = mSystemBus.readByte(opCodeDataAddress);
  Binary::setLower8BitsOf16BitsValue(&mX, value);
  mCpuStatus.updateSignAndZeroFlagFrom8BitValue(value);
}

  void Cpu65816::executeLDX16Bit(OpCode &opCode)
{
  Address opCodeDataAddress = getAddressOfOpCodeData(opCode);
  mX = mSystemBus.readTwoBytes(opCodeDataAddress);
  mCpuStatus.updateSignAndZeroFlagFrom16BitValue(mX);
}

  void Cpu65816::executeLDX(OpCode &opCode)
{
  if (indexIs16BitWide())
  {
    executeLDX16Bit(opCode);
    addToCycles(1);
  }
  else
  {
    executeLDX8Bit(opCode);
  }

  switch (opCode.getCode())
  {
    case (0xA2):                // LDX Immediate
    {
      if (indexIs16BitWide())
      {
        addToProgramAddress(1);
      }
      addToProgramAddressAndCycles(2, 2);
      break;
    }
    case (0xAE):                // LDX Absolute
    {
      addToProgramAddressAndCycles(3, 4);
      break;
    }
    case (0xA6):                // LDX Direct Page
    {
      if (Binary::lower8BitsOf(mD) != 0) {
      addToCycles(1);
    }
      addToProgramAddressAndCycles(2, 3);
      break;
    }
    case (0xBE):                // LDX Absolute Indexed, Y
    {
      if (opCodeAddressingCrossesPageBoundary(opCode)) {
        addToCycles(1);
      }
      addToProgramAddressAndCycles(3, 4);
      break;
    }
    case (0xB6):                // LDX Direct Page Indexed, Y
    {
      if (Binary::lower8BitsOf(mD) != 0) {
      addToCycles(1);
    }
      addToProgramAddressAndCycles(2, 4);
      break;
    }
    default:
    {
      LOG_UNEXPECTED_OPCODE(opCode);
    }
  }
}
}
