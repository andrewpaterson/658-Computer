package net.wdc.wdc65816.instruction.address;

import net.wdc.wdc65816.Address;
import net.wdc.wdc65816.WDC65C816;
import net.util.IntUtil;
import net.wdc.wdc65816.instruction.BusCycleParameter;

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

  public int getBank(WDC65C816 cpu)
  {
    return 0;
  }

  public abstract int getOffset(WDC65C816 cpu);

  public static Address getAddress(WDC65C816 cpu, List<AddressOffset> addressOffsets)
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

