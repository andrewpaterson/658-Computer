package name.bizna.emu65816.opcode;

import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Cpu65816;

public class OpCode_STP
    extends OpCode
{
  public OpCode_STP(String mName, int mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  @Override
  public void executeOnFallingEdge(Cpu65816 cpu)
  {
    cpu.stop();
    cpu.addToProgramAddress(1);
    cpu.addToCycles(3);
  }

  @Override
  public void executeOnRisingEdge(Cpu65816 cpu)
  {
  }
}

