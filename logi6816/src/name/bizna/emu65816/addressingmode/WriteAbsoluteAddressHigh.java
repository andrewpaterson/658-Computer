package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Binary;
import name.bizna.emu65816.Cpu65816;

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
    cpu.getPins().setData(Binary.getHighByte(cpu.getAddress().getOffset()));
  }

  @Override
  public String toString()
  {
    return "Write(AAH)";
  }
}

