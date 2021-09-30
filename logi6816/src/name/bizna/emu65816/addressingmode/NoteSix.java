package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.opcode.OpCode;

public class NoteSix
    extends DataOperation
{
  public NoteSix(boolean notMemoryLock)
  {
    super(false, false, notMemoryLock, true, true);
  }

  @Override
  public void execute(Cpu65816 cpu, OpCode opCode)
  {
  }

  @Override
  public boolean shouldSkipCycle(Cpu65816 cpu)
  {
    //Do not skip if emulation mode and branch taken across page boundaries.
    return true;
  }
}

