package name.bizna.emu65816.opcode;

import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Cpu65816;

import static name.bizna.emu65816.Unsigned.toShort;

public class OpCode_BRL
    extends OpCode
{
  public OpCode_BRL(String mName, int mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  @Override
  public void execute(Cpu65816 cpu, int cycle, boolean clock)
  {
    cpu.addToCycles(executeBranchLongOnCondition(true, cpu));
  }

  int executeBranchLongOnCondition(boolean condition, Cpu65816 cpu)
  {
    if (condition)
    {
      int destination = cpu.readTwoBytes(cpu.getAddressOfOpCodeData(getAddressingMode()));
      cpu.getProgramAddress().incrementOffsetBy(toShort(3 + destination));
    }
    // CPU cycles: 4
    return 4;
  }
}

