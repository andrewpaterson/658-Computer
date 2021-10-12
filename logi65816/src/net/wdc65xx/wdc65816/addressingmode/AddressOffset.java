package net.wdc65xx.wdc65816.addressingmode;

import net.wdc65xx.wdc65816.Address;
import net.wdc65xx.wdc65816.Cpu65816;
import net.util.IntUtil;

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

  public int getBank(Cpu65816 cpu)
  {
    return 0;
  }

  public abstract int getOffset(Cpu65816 cpu);

  public static Address getAddress(Cpu65816 cpu, List<AddressOffset> addressOffsets)
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

