package net.integratedcircuits.wdc.wdc65816.instruction.operations;

import net.integratedcircuits.wdc.wdc65816.W65C816;
import net.util.IntUtil;

public class WriteAbsoluteAddressHigh
    extends DataOperation
{
  public WriteAbsoluteAddressHigh()
  {
    super(false, true, true, false, true);
  }

  @Override
  public void execute(W65C816 cpu)
  {
    setPinData(cpu, IntUtil.getHighByte(cpu.getAddress().getOffset()));
  }

  @Override
  public String toString()
  {
    return "Write(AAH)";
  }
}

