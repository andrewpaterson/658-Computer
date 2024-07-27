package net.logicim.domain.integratedcircuit.wdc.wdc65816.instruction.address;

import net.logicim.domain.integratedcircuit.wdc.wdc65816.Address;
import net.logicim.domain.integratedcircuit.wdc.wdc65816.W65C816;

public class DirectPageAddressPlusX
    extends OffsetAddressCycle
{
  public DirectPageAddressPlusX(int offset)
  {
    super(offset);
  }

  @Override
  public Address getAddress(W65C816 cpu)
  {
    return new Address(0x00, cpu.getDirectPage()).offset(cpu.getDirectOffset() +
                                                         cpu.getX() +
                                                         offset, true);  //@todo - does this wrap, if so where?
  }
}

