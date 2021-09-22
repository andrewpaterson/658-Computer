package name.bizna.emu65816.opcode;

import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Cpu65816;

public class OpCode_WAI
    extends OpCode
{
  public OpCode_WAI(String mName, byte mCode, AddressingMode mAddressingMode)
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
    case(0xCB):     // WAI
    {
      setRDYPin(false);

      addToProgramAddress(1);
      addToCycles(3);
      break;
    }
    default:
    {
      LOG_UNEXPECTED_OPCODE(opCode);
    }
  }
}

}
