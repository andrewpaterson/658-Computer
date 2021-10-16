package net.wdc65xx.wdc65816.instruction.operations;

import net.wdc65xx.wdc65816.Cpu65816;
import net.util.IntUtil;

public class WriteAbsoluteAddressHigh
    extends DataOperation
{
  public WriteAbsoluteAddressHigh()
  {
    super(false, true, true, false, true);
  }

  @Override
  public void execute(Cpu65816 cpu)
  {
    setPinData(cpu, IntUtil.getHighByte(cpu.getAddress().getOffset()));
  }

  @Override
  public String toString()
  {
    return "Write(AAH)";
  }
}

