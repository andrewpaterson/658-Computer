package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BusCycle
{
  protected List<AddressOffset> addressOffsets;
  protected List<Operation> operations;

  public BusCycle(BusCycleParameter... parameters)
  {
    addressOffsets = new ArrayList<>();
    operations = new ArrayList<>();
    for (BusCycleParameter parameter : parameters)
    {
      if (parameter.isAddress())
      {
        addressOffsets.add((AddressOffset) parameter);
      }

      if (parameter.isOperation())
      {
        Operation operation = (Operation) parameter;
        operations.add(operation);
      }
    }

    validate();
  }

  public BusCycle(AddressOffset[] addressOffsets, Operation... operations)
  {
    this.addressOffsets = Arrays.asList(addressOffsets);
    this.operations = Arrays.asList(operations);

    validate();
  }

  private void validate()
  {
    int dataBusOperation = 0;
    for (Operation operation : this.operations)
    {
      if (operation.isData())
      {
        dataBusOperation++;
      }
    }

    if (dataBusOperation != 1)
    {
      throw new EmulatorException("Exactly [1] data bus operation must be specified in a bus cycle.");
    }

    if (addressOffsets.size() == 0)
    {
      throw new EmulatorException("At least [1] address offset must be specified in a bus cycle.");
    }
  }

  public Address getAddress(Cpu65816 cpu)
  {
    return AddressOffset.getAddress(cpu, addressOffsets);
  }

  public DataOperation getDataOperation()
  {
    for (Operation operation : operations)
    {
      if (operation.isData())
      {
        return (DataOperation) operation;
      }
    }
    return null;
  }

  public final void executeOnFallingEdge(Cpu65816 cpu)
  {
    Address address = getAddress(cpu);
    getDataOperation().setPins(cpu);

    Pins pins = cpu.getPins();
    pins.setAddress(address.getOffset());
    pins.setData(address.getBank());
  }

  public final void executeOnRisingEdge(Cpu65816 cpu)
  {
    getDataOperation().setPins(cpu);

    for (Operation operation : operations)
    {
      operation._execute(cpu);
    }
  }
}

