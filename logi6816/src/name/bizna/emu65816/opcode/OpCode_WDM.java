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

  protected void executeMisc(Cpu65816 cpu)
{
  switch (getCode()) {
   case(0x42):     // WDM
    {
      cpu.addToProgramAddress(2);
      cpu.addToCycles(2);
      break;
    }
    default:
throw new IllegalStateException("Unexpected value: " + getCode());  }
}

}
