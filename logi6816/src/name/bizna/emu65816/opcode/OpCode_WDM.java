package name.bizna.emu65816.opcode;

import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Cpu65816;

public class OpCode_WDM
    extends OpCode
{
  public OpCode_WDM(String mName, byte mCode, AddressingMode mAddressingMode)
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
   case(0x42):     // WDM
    {
      addToProgramAddress(2);
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
