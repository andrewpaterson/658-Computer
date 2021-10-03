package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.addressingmode.InstructionCycles;

import static name.bizna.emu65816.opcode.Branch.executeBranchShortOnCondition;

public class OpCode_BMI
    extends OpCode
{
  public OpCode_BMI(int mCode, InstructionCycles cycles)
  {
    super("BMI", "Branch if Result Minus (N=1)", mCode, cycles);
  }

  @Override
  public void executeOnFallingEdge(Cpu65816 cpu)
  {
    cpu.addToCycles(executeBranchShortOnCondition(cpu.getCpuStatus().signFlag(), cpu, getAddressingMode()));
  }
}

