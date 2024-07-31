package net.logicim.domain.integratedcircuit.wdc.wdc65816.instruction.operations;

import net.common.util.IntUtil;
import net.logicim.domain.integratedcircuit.wdc.wdc65816.W65C816;

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
    cpu.setData(IntUtil.getHighByte(cpu.getState().getAddress().getOffset()));
  }

  @Override
  public String toString()
  {
    return "Write(AAH)";
  }
}

