package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.addressingmode.InstructionCycles;

import static name.bizna.emu65816.opcode.Branch.executeBranchShortOnCondition;

public class OpCode_BVS
    extends OpCode
{
  public OpCode_BVS(int mCode, InstructionCycles cycles)
  {
    super("BVS", "Branch on Overflow Set (V=1)", mCode, cycles);
  }

  @Override
  public void executeOnFallingEdge(Cpu65816 cpu)
  {
    cpu.addToCycles(executeBranchShortOnCondition(cpu.getCpuStatus().overflowFlag(), cpu, getAddressingMode()));
  }
}

