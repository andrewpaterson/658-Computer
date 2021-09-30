package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.opcode.OpCode;

public class Fetch_New_PCH
    extends DataOperation
{
  public Fetch_New_PCH(boolean notMemoryLock)
  {
    super(false, true, notMemoryLock, true, true);
  }

  @Override
  public void execute(Cpu65816 cpu, OpCode opCode)
  {
    cpu.setNewProgramCounterHigh(cpu.getPinData());
  }
}

