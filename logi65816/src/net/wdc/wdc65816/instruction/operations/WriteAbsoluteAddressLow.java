package net.wdc.wdc65816.instruction.operations;

import net.wdc.wdc65816.WDC65C816;
import net.util.IntUtil;

public class WriteAbsoluteAddressLow
    extends DataOperation
{
  public WriteAbsoluteAddressLow()
  {
    super(false, true, true, false, true);
  }

  @Override
  public void execute(WDC65C816 cpu)
  {
    setPinData(cpu, IntUtil.getLowByte(cpu.getAddress().getOffset()));
  }

  @Override
  public String toString()
  {
    return "Write(AAL)";
  }
}

