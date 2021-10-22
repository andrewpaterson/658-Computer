package net.wdc.wdc65816.instruction;

import net.util.EmulatorException;
import net.wdc.wdc65816.Address;
import net.wdc.wdc65816.WDC65816Pins;
import net.wdc.wdc65816.WDC65816;
import net.wdc.wdc65816.instruction.address.AddressOffset;
import net.wdc.wdc65816.instruction.address.ConstantOffset;
import net.wdc.wdc65816.instruction.operations.DataOperation;
import net.wdc.wdc65816.instruction.operations.Operation;

import java.util.Arrays;
import java.util.List;

public class BusCycle
{
  protected List<AddressOffset> addressOffsets;
  protected List<Operation> operations;
  protected int cycle;

  public BusCycle(AddressOffset[] addressOffsets, Operation... operations)
  {
    this.addressOffsets = Arrays.asList(addressOffsets);
    this.operations = Arrays.asList(operations);
    this.cycle = -1;

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

  public Address getAddress(WDC65816 cpu)
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

  public int getDone8()
  {
    int done8 = 0;
    for (Operation operation : operations)
    {
      done8 += operation.getDone8();
    }
    return done8;
  }

  public int getDone16()
  {
    int done16 = 0;
    for (Operation operation : operations)
    {
      done16 += operation.getDone16();
    }
    return done16;
  }

  public final void executeFirstHalf(WDC65816 cpu)
  {
    DataOperation dataOperation = getDataOperation();
    boolean read = dataOperation.isRead();
    Address address = getAddress(cpu);

    cpu.setRead(read);

    WDC65816Pins pins = cpu.getPins();
    pins.setMX(cpu.isIndex8Bit());
    pins.setRWB(read);
    pins.setValidDataAddress(dataOperation.isValidDataAddress());
    pins.setValidProgramAddress(dataOperation.isValidProgramAddress());
    pins.setMemoryLockB(dataOperation.isNotMemoryLock());
    pins.setVectorPullB(dataOperation.isNotVectorPull());
    pins.setRdy(dataOperation.isReady());
    pins.setEmulation(cpu.isEmulation());
    pins.setAddress(address.getOffset());
    pins.setBank(address.getBank());
  }

  public final void executeSecondHalf(WDC65816 cpu)
  {
    DataOperation dataOperation = getDataOperation();
    boolean read = dataOperation.isRead();
    Address address = getAddress(cpu);

    cpu.setRead(read);

    WDC65816Pins pins = cpu.getPins();
    pins.setRWB(read);
    pins.setMemoryLockB(dataOperation.isNotMemoryLock());
    pins.setMX(cpu.isMemory8Bit());
    pins.setEmulation(cpu.isEmulation());
    pins.setValidDataAddress(dataOperation.isValidDataAddress());
    pins.setValidProgramAddress(dataOperation.isValidProgramAddress());
    pins.setVectorPullB(dataOperation.isNotVectorPull());
    pins.setRdy(dataOperation.isReady());
    pins.setAddress(address.getOffset());

    for (Operation operation : operations)
    {
      operation.execute(cpu);
    }

    cpu.nextCycle();
  }

  public boolean mustExecute(WDC65816 cpu)
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
      if (!first && !(addressOffset instanceof ConstantOffset))
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

  public String toDataString()
  {
    DataOperation operation = getDataOperation();
    return operation.toString();
  }

  public boolean isFetchOpCode()
  {
    DataOperation dataOperation = getDataOperation();
    return dataOperation.isFetchOpCode();
  }

  public void setCycle(int cycle)
  {
    this.cycle = cycle;
  }

  public int getCycle()
  {
    return cycle;
  }
}

