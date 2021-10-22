package net.wdc.wdc65816.instruction.operations;

import net.wdc.wdc65816.WDC65816;
import net.util.IntUtil;

public class WriteProgramCounterHigh
    extends DataOperation
{
  public WriteProgramCounterHigh()
  {
    super(false, true, true, false, true);
  }

  @Override
  public void execute(WDC65816 cpu)
  {
    setPinData(cpu, IntUtil.getHighByte(cpu.getProgramCounter().getOffset()));
  }

  @Override
  public String toString()
  {
    return "Write(PCH)";
  }
}

