package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.addressingmode.InstructionCycles;

import static name.bizna.emu65816.Unsigned.toShort;

public class OpCode_BRL
    extends OpCode
{
  public OpCode_BRL(int mCode, InstructionCycles cycles)
  {
    super("BRL", "Branch Always Long", mCode, cycles);
  }

  @Override
  public void executeOnFallingEdge(Cpu65816 cpu)
  {
    cpu.addToCycles(executeBranchLongOnCondition(true, cpu));
  }

  int executeBranchLongOnCondition(boolean condition, Cpu65816 cpu)
  {
    if (condition)
    {
      int destination = cpu.getData(cpu.getAddressOfOpCodeData(getAddressingMode()));
      cpu.getProgramCounter().incrementOffsetBy(toShort(3 + destination));
    }
    // CPU cycles: 4
    return 4;
  }
}

