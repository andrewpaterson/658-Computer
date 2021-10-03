package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.addressingmode.InstructionCycles;

import static name.bizna.emu65816.opcode.Branch.executeBranchShortOnCondition;

public class OpCode_BVC
    extends OpCode
{
  public OpCode_BVC(int mCode, InstructionCycles cycles)
  {
    super("BVC", "Branch on Overflow Clear (V=0)", mCode, cycles);
  }

  @Override
  public void executeOnFallingEdge(Cpu65816 cpu)
  {
    cpu.addToCycles(executeBranchShortOnCondition(!cpu.getCpuStatus().overflowFlag(), cpu, getAddressingMode()));
  }
}

