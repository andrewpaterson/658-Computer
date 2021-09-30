package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.opcode.OpCode;

import static name.bizna.emu65816.Unsigned.toByte;

public class DirectPageLowZero
    extends DataOperation
{
  public DirectPageLowZero(boolean notMemoryLock)
  {
    super(false, false, notMemoryLock, true, true);
  }

  @Override
  public boolean shouldSkipCycle(Cpu65816 cpu)
  {
    return toByte(cpu.getDirectPage()) == 0;
  }

  @Override
  public void execute(Cpu65816 cpu, OpCode opCode)
  {
  }
}

