package name.bizna.emu65816.opcode;

import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Cpu65816;

public class OpCode_CPX
    extends OpCode
{
  public OpCode_CPX(String mName, byte mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  @Override
  public void execute(Cpu65816 cpu)
  {

  }

  void Cpu65816::execute8BitCPX(OpCode &opCode)
{
  uint8_t value = mSystemBus.readByte(getAddressOfOpCodeData(opCode));
  uint8_t result = Binary::lower8BitsOf(mX) - value;
  mCpuStatus.updateSignAndZeroFlagFrom8BitValue(result);
  bool carry = Binary::lower8BitsOf(mX) >= value;
  mCpuStatus.setCarryFlag(carry);
}

  void Cpu65816::execute16BitCPX(OpCode &opCode)
{
  uint16_t value = mSystemBus.readTwoBytes(getAddressOfOpCodeData(opCode));
  uint16_t result = mX - value;
  mCpuStatus.updateSignAndZeroFlagFrom16BitValue(result);
  bool carry = mX >= value;
  mCpuStatus.setCarryFlag(carry);
}

  void Cpu65816::execute8BitCPY(OpCode &opCode)
{
  uint8_t value = mSystemBus.readByte(getAddressOfOpCodeData(opCode));
  uint8_t result = Binary::lower8BitsOf(mY) - value;
  mCpuStatus.updateSignAndZeroFlagFrom8BitValue(result);
  bool carry = Binary::lower8BitsOf(mY) >= value;
  mCpuStatus.setCarryFlag(carry);
}

  void Cpu65816::execute16BitCPY(OpCode &opCode)
{
  uint16_t value = mSystemBus.readTwoBytes(getAddressOfOpCodeData(opCode));
  uint16_t result = mY - value;
  mCpuStatus.updateSignAndZeroFlagFrom16BitValue(result);
  bool carry = mY >= value;
  mCpuStatus.setCarryFlag(carry);
}

  void Cpu65816::executeCPXCPY(OpCode &opCode)
{
  switch (opCode.getCode())
  {
    case(0xE0):  // CPX Immediate
    {
      if (indexIs8BitWide())
      {
        execute8BitCPX(opCode);
      }
      else
      {
        execute16BitCPX(opCode);
        addToProgramAddress(1);
        addToCycles(1);
      }
      addToProgramAddressAndCycles(2, 2);
      break;
    }
    case(0xEC):  // CPX Absolute
    {
      if (indexIs8BitWide())
      {
        execute8BitCPX(opCode);
      }
      else
      {
        execute16BitCPX(opCode);
        addToCycles(1);
      }
      addToProgramAddressAndCycles(3, 4);
      break;
    }
    case(0xE4):  // CPX Direct Page
    {
      if (indexIs8BitWide())
      {
        execute8BitCPX(opCode);
      }
      else
      {
        execute16BitCPX(opCode);
        addToCycles(1);
      }
      if (Binary::lower8BitsOf(mD) != 0)
      {
        addToCycles(1);
      }
      addToProgramAddressAndCycles(2, 3);
      break;
    }
    case(0xC0):  // CPY Immediate
    {
      if (indexIs8BitWide())
      {
        execute8BitCPY(opCode);
      }
      else
      {
        execute16BitCPY(opCode);
        addToProgramAddress(1);
        addToCycles(1);
      }
      addToProgramAddressAndCycles(2, 2);
      break;
    }
    case(0xCC):  // CPY Absolute
    {
      if (indexIs8BitWide())
      {
        execute8BitCPY(opCode);
      }
      else
      {
        execute16BitCPY(opCode);
        addToCycles(1);
      }
      addToProgramAddressAndCycles(3, 4);
      break;
    }
    case(0xC4):  // CPY Direct Page
    {
      if (indexIs8BitWide())
      {
        execute8BitCPY(opCode);
      }
      else
      {
        execute16BitCPY(opCode);
        addToCycles(1);
      }
      if (Binary::lower8BitsOf(mD) != 0)
      {
        addToCycles(1);
      }
      addToProgramAddressAndCycles(2, 3);
      break;
    }
    default:
    {
      LOG_UNEXPECTED_OPCODE(opCode);
    }
  }
}
}
