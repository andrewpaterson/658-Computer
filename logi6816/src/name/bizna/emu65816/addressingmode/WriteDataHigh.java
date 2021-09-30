package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.opcode.OpCode;

public class WriteDataHigh
    extends DataOperation
{
  public WriteDataHigh(boolean notMemoryLock)
  {
    super(false, true, notMemoryLock, false, true);
  }

  @Override
  public boolean shouldSkipCycle(Cpu65816 cpu)
  {
    return cpu.isMemory8Bit();
  }

  @Override
  public void execute(Cpu65816 cpu, OpCode opCode)
  {
    cpu.setPinsData(cpu.getDataHigh());
  }
}

