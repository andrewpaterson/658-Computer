package name.bizna.cpu.addressingmode;

import name.bizna.cpu.Cpu65816;
import name.bizna.util.IntUtil;

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
    cpu.getPins().setData(IntUtil.getHighByte(cpu.getAddress().getOffset()));
  }

  @Override
  public String toString()
  {
    return "Write(AAH)";
  }
}

