package net.wdc65xx.wdc65816.addressingmode;

import net.wdc65xx.wdc65816.Cpu65816;
import net.util.IntUtil;

public class WriteAbsoluteAddressLow
    extends DataOperation
{
  public WriteAbsoluteAddressLow()
  {
    super(false, true, true, false, true);
  }

  @Override
  public void execute(Cpu65816 cpu)
  {
    cpu.getPins().setData(IntUtil.getLowByte(cpu.getAddress().getOffset()));
  }

  @Override
  public String toString()
  {
    return "Write(AAL)";
  }
}

