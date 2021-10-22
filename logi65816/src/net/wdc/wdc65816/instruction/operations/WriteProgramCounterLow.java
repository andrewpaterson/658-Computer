package net.wdc.wdc65816.instruction.operations;

import net.wdc.wdc65816.WDC65816;
import net.util.IntUtil;

public class WriteProgramCounterLow
    extends DataOperation
{
  public WriteProgramCounterLow()
  {
    super(false, true, true, false, true);
  }

  @Override
  public void execute(WDC65816 cpu)
  {
    setPinData(cpu, IntUtil.getLowByte(cpu.getProgramCounter().getOffset()));
  }

  @Override
  public String toString()
  {
    return "Write(PCL)";
  }
}

