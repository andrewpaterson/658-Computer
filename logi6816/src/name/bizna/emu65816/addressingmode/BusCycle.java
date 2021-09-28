package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Address;
import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.Unsigned;

import java.util.ArrayList;
import java.util.List;

public class BusCycle
{
  protected List<AddressOffset> addressOffsets;
  protected List<CycleOperation> operations;

  public BusCycle(BusCycleParameter... parameters)
  {
    addressOffsets = new ArrayList<>();
    operations = new ArrayList<>();
    for (BusCycleParameter parameter : parameters)
    {
      if (parameter.isAddressOffset())
      {
        addressOffsets.add((AddressOffset) parameter);
      }
      if (parameter.isOperation())
      {
        operations.add((CycleOperation) parameter);
      }
    }
  }

  public Address getAddress(Cpu65816 cpu)
  {
    int bank = 0;
    int offset = 0;
    for (AddressOffset addressOffset : addressOffsets)
    {
      offset += addressOffset.getOffset(cpu);
      bank += addressOffset.getBank(cpu);
    }
    return new Address(Unsigned.toByte(bank), Unsigned.toShort(offset));
  }
}

