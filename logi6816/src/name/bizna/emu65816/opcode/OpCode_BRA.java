package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.addressingmode.InstructionCycles;

import static name.bizna.emu65816.opcode.Branch.executeBranchShortOnCondition;

public class OpCode_BRA
    extends OpCode
{
  public OpCode_BRA(int mCode, InstructionCycles cycles)
  {
    super("BRA", "Branch Always", mCode, cycles);
  }

  @Override
  public void executeOnFallingEdge(Cpu65816 cpu)
  {
    cpu.addToCycles(executeBranchShortOnCondition(true, cpu, getAddressingMode()));
  }
}

