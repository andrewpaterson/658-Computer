package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Address;
import name.bizna.emu65816.Cpu65816;

public class DirectPageAddressPlusX
    extends OffsetAddressCycle
{
  public DirectPageAddressPlusX(int offset)
  {
    super(offset);
  }

  @Override
  public Address getAddress(Cpu65816 cpu)
  {
    return new Address(0x00, cpu.getDirectPage()).offset(cpu.getDirectOffset() +
                                                         cpu.getX() +
                                                         offset, true);  //@todo - does this wrap, if so where?
  }
}

