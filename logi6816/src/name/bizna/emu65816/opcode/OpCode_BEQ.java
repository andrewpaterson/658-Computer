package name.bizna.emu65816.opcode;

import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Cpu65816;

import static name.bizna.emu65816.opcode.Branch.executeBranchShortOnCondition;

public class OpCode_BEQ
    extends OpCode
{
  public OpCode_BEQ(String mName, int mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  @Override
  public void execute(Cpu65816 cpu, int cycle, boolean clock)
  {
    cpu.addToCycles(executeBranchShortOnCondition(cpu.getCpuStatus().zeroFlag(), cpu, getAddressingMode()));
  }
}
