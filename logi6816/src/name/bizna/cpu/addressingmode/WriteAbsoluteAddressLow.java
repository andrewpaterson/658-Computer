package name.bizna.cpu.addressingmode;

import name.bizna.cpu.Cpu65816;
import name.bizna.util.IntUtil;

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

