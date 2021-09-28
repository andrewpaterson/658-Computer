package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.EmulatorException;

public class Offset
    extends AddressOffset
{
  private int offset;

  public Offset(int offset)
  {
    if (offset <0 || offset >3)
    {
      throw new EmulatorException("Numeric Offset must be in the range 0...3.");
    }
    this.offset = offset;
  }

  @Override
  public int getOffset(Cpu65816 cpu)
  {
    return offset;
  }
}

