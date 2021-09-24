package name.bizna.emu65816.opcode;

import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Cpu65816;

public class OpCode_CLI
    extends OpCode
{
  public OpCode_CLI(String mName, int mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  @Override
  public void execute(Cpu65816 cpu, int cycle, boolean clock)
  {
    cpu.getCpuStatus().setInterruptDisableFlag(false);
    cpu.addToProgramAddressAndCycles(1, 2);
  }
}
