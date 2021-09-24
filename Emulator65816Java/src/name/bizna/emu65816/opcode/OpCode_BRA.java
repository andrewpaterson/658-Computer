package name.bizna.emu65816.opcode;

import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Cpu65816;

import static name.bizna.emu65816.opcode.Branch.executeBranchShortOnCondition;

public class OpCode_BRA
    extends OpCode
{
  public OpCode_BRA(String mName, byte mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  @Override
  public void execute(Cpu65816 cpu)
  {
    cpu.addToCycles(executeBranchShortOnCondition(true, cpu, getAddressingMode()));
  }
}

