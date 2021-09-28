package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Address;
import name.bizna.emu65816.Cpu65816;

public class DirectPageAddress
    extends OffsetAddressCycle
{
  public DirectPageAddress(int offset)
  {
    super(offset);
  }

  @Override
  public Address getAddress(Cpu65816 cpu)
  {
    cpu.getDirectOffset()
    return new Address(0x00, cpu.getDirectPage()).offset(offset, true);  //@todo - does this wrap;
  }
}

