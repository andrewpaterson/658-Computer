package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.addressingmode.InstructionCycles;

import static name.bizna.emu65816.opcode.Branch.executeBranchShortOnCondition;

public class OpCode_BCC
    extends OpCode
{
  public OpCode_BCC(int mCode, InstructionCycles cycles)
  {
    super("BCC", "Branch on Carry Clear (C=0)", mCode, cycles);
  }

  @Override
  public void executeOnFallingEdge(Cpu65816 cpu)
  {
    cpu.addToCycles(executeBranchShortOnCondition(!cpu.getCpuStatus().carryFlag(), cpu, getAddressingMode()));
  }
}

