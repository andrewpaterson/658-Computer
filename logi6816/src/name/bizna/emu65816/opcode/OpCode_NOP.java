package name.bizna.emu65816.opcode;

import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Cpu65816;

public class OpCode_NOP
    extends OpCode
{
  public OpCode_NOP(String mName, byte mCode, AddressingMode mAddressingMode)
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
    case(0xEA):     // NOP
    {
      addToProgramAddress(1);
      addToCycles(2);
      break;
    }
    default:
    {
      LOG_UNEXPECTED_OPCODE(opCode);
    }
  }
}

}
