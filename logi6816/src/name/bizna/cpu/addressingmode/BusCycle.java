package name.bizna.cpu.addressingmode;

import name.bizna.cpu.Address;
import name.bizna.cpu.Cpu65816;
import name.bizna.cpu.Pins65816;
import name.bizna.util.EmulatorException;

import java.util.Arrays;
import java.util.List;

public class BusCycle
{
  protected List<AddressOffset> addressOffsets;
  protected List<Operation> operations;

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

  public Operation getDoneOperation()
  {
    for (Operation operation : operations)
    {
      if (operation.isDone())
      {
        return operation;
      }
    }
    return null;
  }

  public final void executeOnFallingEdge(Cpu65816 cpu)
  {
    DataOperation dataOperation = getDataOperation();
    boolean read = dataOperation.isRead();
    cpu.setRead(read);
    Pins65816 pins = cpu.getPins();
    pins.setRwb(read);
    pins.setMemoryLockB(dataOperation.isNotMemoryLock());
    pins.setMX(cpu.isMemory8Bit());
    pins.setEmulation(cpu.isEmulation());
    pins.setValidDataAddress(dataOperation.isValidDataAddress());
    pins.setValidProgramAddress(dataOperation.isValidProgramAddress());
    pins.setVectorPullB(dataOperation.isNotVectorPull());
    pins.setRdy(dataOperation.isReady());

    //Address is always valid before the operation.
    Address address = getAddress(cpu);
    pins.setAddress(address.getOffset());

    for (Operation operation : operations)
    {
      operation.execute(cpu);
    }
  }

  public final void executeOnRisingEdge(Cpu65816 cpu)
  {
    Address address = getAddress(cpu);
    DataOperation dataOperation = getDataOperation();
    boolean read = cpu.isRead();

    Pins65816 pins = cpu.getPins();
    pins.setMX(cpu.isIndex8Bit());
    pins.setRwb(read);
    pins.setValidDataAddress(dataOperation.isValidDataAddress());
    pins.setValidProgramAddress(dataOperation.isValidProgramAddress());
    pins.setMemoryLockB(dataOperation.isNotMemoryLock());
    pins.setVectorPullB(dataOperation.isNotVectorPull());
    pins.setRdy(dataOperation.isReady());
    pins.setEmulation(cpu.isEmulation());
    pins.setAddress(address.getOffset());
    pins.setData(address.getBank());
  }

  public boolean mustExecute(Cpu65816 cpu)
  {
    boolean mustExecute = true;
    for (Operation operation : operations)
    {
      if (!operation.mustExecute(cpu))
      {
        mustExecute = false;
      }
    }
    return mustExecute;
  }

  public String toAddressOffsetString()
  {
    StringBuilder string = new StringBuilder();
    boolean first = true;
    for (AddressOffset addressOffset : addressOffsets)
    {
      if (!first && !(addressOffset instanceof Offset))
      {
        if (string.charAt(string.length() - 1) != ',')
        {
          string.append("+");
        }
      }
      string.append(addressOffset.toString());

      first = false;
    }
    return string.toString();
  }

  public String toOperationString()
  {
    StringBuilder string = new StringBuilder();
    boolean first = true;
    for (Operation operation : operations)
    {
      if (!first)
      {
        string.append(" ");
      }
      string.append(operation.toString());
      first = false;
    }
    return string.toString();
  }
}

