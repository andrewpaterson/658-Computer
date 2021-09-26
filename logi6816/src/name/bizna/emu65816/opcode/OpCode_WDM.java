package name.bizna.emu65816.opcode;

import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Cpu65816;

public class OpCode_WDM
    extends OpCode
{
  public OpCode_WDM(String mName, int mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  @Override
  public void executeOnFallingEdge(Cpu65816 cpu)
  {
    cpu.addToProgramAddress(2);
    cpu.addToCycles(2);
  }

  @Override
  public void executeOnRisingEdge(Cpu65816 cpu)
  {
  }
}

