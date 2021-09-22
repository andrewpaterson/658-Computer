package name.bizna.emu65816.opcode;

import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Cpu65816;

public class OpCode_XBA
    extends OpCode
{
  public OpCode_XBA(String mName, byte mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  @Override
  public void execute(Cpu65816 cpu)
  {

  }

  void Cpu65816::executeMisc(OpCode &opCode)
{
  switch (opCode.getCode()) {
    case(0xEB):     // XBA
    {
      uint8_t lowerA = Binary::lower8BitsOf(mA);
      uint8_t higherA = Binary::higher8BitsOf(mA);
      mA = higherA | (((uint16_t)(lowerA)) << 8);
      mCpuStatus.updateSignAndZeroFlagFrom8BitValue(higherA);
      addToProgramAddressAndCycles(1, 3);
      break;
    }
      default:
    {
      LOG_UNEXPECTED_OPCODE(opCode);
    }
  }
}

}
