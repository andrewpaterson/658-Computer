package name.bizna.emu65816.opcode;

import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Cpu65816;

import static name.bizna.emu65816.opcode.Branch.executeBranchShortOnCondition;

public class OpCode_BVS
    extends OpCode
{
  public OpCode_BVS(String mName, int mCode, InstructionCycles cycles)
  {
    super(mName, mCode, cycles);
  }

  @Override
  public void executeOnFallingEdge(Cpu65816 cpu)
  {
    cpu.addToCycles(executeBranchShortOnCondition(cpu.getCpuStatus().overflowFlag(), cpu, getAddressingMode()));
  }

  @Override
  public void executeOnRisingEdge(Cpu65816 cpu)
  {
  }
}

