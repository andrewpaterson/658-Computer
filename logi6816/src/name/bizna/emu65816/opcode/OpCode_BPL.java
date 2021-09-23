package name.bizna.emu65816.opcode;

import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Cpu65816;

public class OpCode_BPL
    extends OpCodeBranch
{
  public OpCode_BPL(String mName, byte mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  @Override
  public void execute(Cpu65816 cpu)
  {
    int cycles = executeBranchShortOnCondition(!cpu.getCpuStatus().signFlag(), cpu);
    cpu.addToCycles(cycles);
  }
}
