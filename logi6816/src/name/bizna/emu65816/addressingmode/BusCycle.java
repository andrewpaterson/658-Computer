package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Address;
import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.EmulatorException;

import java.util.ArrayList;
import java.util.List;

public class BusCycle
{
  protected List<AddressOffset> addressOffsets;
  protected List<CycleOperation> operations;

  public BusCycle(BusCycleParameter... parameters)
  {
    int dataBusOperation = 0;

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
        CycleOperation cycleOperation = (CycleOperation) parameter;
        operations.add(cycleOperation);

        if (cycleOperation.isDataBus())
        {
          dataBusOperation++;
        }
      }
    }

    if (dataBusOperation != 1)
    {
      throw new EmulatorException("Exactly [1] data bus operation must be specified in a bus cycle.");
    }
  }

  public Address getAddress(Cpu65816 cpu)
  {
    return AddressOffset.getAddress(cpu, addressOffsets);
  }
}

