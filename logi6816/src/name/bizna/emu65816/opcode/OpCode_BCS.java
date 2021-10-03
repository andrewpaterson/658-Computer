package name.bizna.emu65816.opcode;

import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.addressingmode.InstructionCycles;

import static name.bizna.emu65816.opcode.Branch.executeBranchShortOnCondition;

public class OpCode_BCS
    extends OpCode
{
  public OpCode_BCS(int mCode, InstructionCycles cycles)
  {
    super("BCS", "Branch on Carry Set (C=1)", mCode, cycles);
  }

  @Override
  public void executeOnFallingEdge(Cpu65816 cpu)
  {
    cpu.addToCycles(executeBranchShortOnCondition(cpu.getCpuStatus().carryFlag(), cpu, getAddressingMode()));
  }
}
