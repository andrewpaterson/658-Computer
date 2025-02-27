package net.logicim.domain.integratedcircuit.wdc.wdc65816.instruction.address;

import net.common.util.IntUtil;
import net.logicim.domain.integratedcircuit.wdc.wdc65816.Address;
import net.logicim.domain.integratedcircuit.wdc.wdc65816.W65C816;
import net.logicim.domain.integratedcircuit.wdc.wdc65816.instruction.BusCycleParameter;

import java.util.List;

public abstract class AddressOffset
    implements BusCycleParameter
{
  @Override
  public boolean isOperation()
  {
    return false;
  }

  @Override
  public boolean isAddress()
  {
    return true;
  }

  public int getBank(W65C816 cpu)
  {
    return 0;
  }

  public abstract int getOffset(W65C816 cpu);

  public static Address getAddress(W65C816 cpu, List<AddressOffset> addressOffsets)
  {
    int bank = 0;
    int offset = 0;
    for (AddressOffset addressOffset : addressOffsets)
    {
      offset += addressOffset.getOffset(cpu);
      bank += addressOffset.getBank(cpu);
    }
    return new Address(IntUtil.toByte(bank), IntUtil.toShort(offset));
  }
}

