package net.wdc65xx.wdc65816.instruction.address;

import net.wdc65xx.wdc65816.Address;
import net.wdc65xx.wdc65816.Cpu65816;

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
