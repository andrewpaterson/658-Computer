package net.logicim.domain.integratedcircuit.wdc.wdc65816.instruction.operation;

import net.common.util.IntUtil;
import net.logicim.domain.integratedcircuit.wdc.wdc65816.W65C816;

public class WriteAbsoluteAddressLow
    extends DataOperation
{
  public WriteAbsoluteAddressLow()
  {
    super(false, true, true, false, true);
  }

  @Override
  public void execute(W65C816 cpu)
  {
    cpu.setData(IntUtil.getLowByte(cpu.getState().getAddress().getOffset()));
  }

  @Override
  public String toString()
  {
    return "Write(AAL)";
  }
}

