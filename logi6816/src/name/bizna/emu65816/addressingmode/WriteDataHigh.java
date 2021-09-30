package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.opcode.OpCode;

public class FetchDataHigh
    extends DataOperation
{
  public FetchDataHigh(boolean notMemoryLock)
  {
    super(false, true, notMemoryLock, true, true);
  }

  @Override
  public boolean shouldSkipCycle(Cpu65816 cpu)
  {
    return cpu.isMemory8Bit();
  }

  @Override
  public void execute(Cpu65816 cpu, OpCode opCode)
  {
    cpu.setDataHigh(cpu.getPinData());
  }
}

