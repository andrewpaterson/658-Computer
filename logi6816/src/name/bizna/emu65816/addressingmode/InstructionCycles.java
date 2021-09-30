package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.Address;
import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.interrupt.InterruptVector;
import name.bizna.emu65816.opcode.OpCode;

import java.util.Arrays;
import java.util.List;

public abstract class InstructionCycles
{
  protected static final boolean RMW = false;

  protected List<BusCycle> cycles;
  protected AddressingMode addressingMode;

  public InstructionCycles(AddressingMode addressingMode,
                           BusCycle... cycles)
  {
    this.addressingMode = addressingMode;
    this.cycles = Arrays.asList(cycles);
  }

  protected static ProgramCounter PC()
  {
    return new ProgramCounter();
  }

  protected static StackPointer S()
  {
    return new StackPointer();
  }

  protected static StackOffset StackOffset()
  {
    return new StackOffset();
  }

  protected static AddressOffset[] Address(AddressOffset... addressOffsets)
  {
    return addressOffsets;
  }

  protected static Operation[] Operation(Operation... operations)
  {
    return operations;
  }

  protected static Operation[] ExecuteLow(boolean read, boolean notMemoryLock)
  {
    if (read)
    {
      return InstructionCycles.Operation(new FetchDataLow(notMemoryLock), new Execute1());
    }
    else
    {
      return InstructionCycles.Operation(new Execute1(), new WriteDataLow(notMemoryLock));
    }
  }

  protected static Operation[] ExecuteHigh(boolean read, boolean notMemoryLock)
  {
    if (read)
    {
      return InstructionCycles.Operation(new FetchDataHigh(notMemoryLock), new Execute2());
    }
    else
    {
      return InstructionCycles.Operation(new Execute2(), new WriteDataHigh(notMemoryLock));
    }
  }

  protected static ProgramBank PBR()
  {
    return new ProgramBank();
  }

  protected static AbsoluteAddress AA()
  {
    return new AbsoluteAddress();
  }

  protected static XIndex X()
  {
    return new XIndex();
  }

  protected static YIndex Y()
  {
    return new YIndex();
  }

  protected static Offset o(int offset)
  {
    return new Offset(offset);
  }

  protected static DataBank DBR()
  {
    return new DataBank();
  }

  protected static AbsoluteAddressHigh AAH()
  {
    return new AbsoluteAddressHigh();
  }

  protected static InterruptAddress InterruptAddress(InterruptVector interruptVector)
  {
    return new InterruptAddress(interruptVector);
  }

  protected static DirectOffset DirectOffset()
  {
    return new DirectOffset();
  }

  protected static DirectPage DirectPage()
  {
    return new DirectPage();
  }

  protected static NewProgramCounter New_PC()
  {
    return new NewProgramCounter();
  }

  protected static NewProgramBank New_PBR()
  {
    return new NewProgramBank();
  }

  protected static AddressBank AAB()
  {
    return new AddressBank();
  }

  protected static FetchOpCode OpCode()
  {
    return new FetchOpCode();
  }

  protected static IncrementProgramCounter PC_pp()
  {
    return new IncrementProgramCounter();
  }

  protected static DecrementStackPointer SP_mm()
  {
    return new DecrementStackPointer();
  }

  protected static WriteProgramBank Write_PBR()
  {
    return new WriteProgramBank();
  }

  protected static FetchNewProgramCounterHigh Read_NewPCH()
  {
    return new FetchNewProgramCounterHigh(true);
  }

  protected static InternalOperation IO()
  {
    return new InternalOperation(true);
  }

  protected static SetProgramCounter PC_e(AddressOffset... addressOffsets)
  {
    return new SetProgramCounter(addressOffsets);
  }

  protected static WriteProgramCounterLow Write_PCL()
  {
    return new WriteProgramCounterLow();
  }

  protected static WriteProgramCounterHigh Write_PCH()
  {
    return new WriteProgramCounterHigh();
  }

  protected static FetchAbsoluteAddressHigh Read_AAH()
  {
    return new FetchAbsoluteAddressHigh(true);
  }

  protected static FetchAbsoluteAddressHigh Read_AAH(boolean notMemoryLock)
  {
    return new FetchAbsoluteAddressHigh(notMemoryLock);
  }

  protected static FetchNewProgramBank Read_NewPBR()
  {
    return new FetchNewProgramBank(true);
  }

  protected static FetchNewProgramCounterLow Read_NewPCL()
  {
    return new FetchNewProgramCounterLow(true);
  }

  protected static FetchAbsoluteAddressLow Read_AAL()
  {
    return new FetchAbsoluteAddressLow(true);
  }

  protected static AbsoluteAddressLowPlusXLow AAL_XL()
  {
    return new AbsoluteAddressLowPlusXLow();
  }

  protected static AbsoluteAddressLowPlusYLow AAL_YL()
  {
    return new AbsoluteAddressLowPlusYLow();
  }

  protected static DirectOffset D0()
  {
    return DirectOffset();
  }

  protected static DirectPageLowZero DPL_ne_0()
  {
    return new DirectPageLowZero(true);
  }

  protected static FetchDirectOffset Read_D0()
  {
    return new FetchDirectOffset(true);
  }

  protected static FetchAbsoluteAddressBank Read_AAB()
  {
    return new FetchAbsoluteAddressBank(true);
  }

  protected static WriteProcessorStatus Write_PS()
  {
    return new WriteProcessorStatus();
  }

  protected static FetchAbsoluteAddressLow Read_AAL(boolean notMemoryLock)
  {
    return new FetchAbsoluteAddressLow(notMemoryLock);
  }

  protected static InternalOperation IO(boolean notMemoryLock)
  {
    return new InternalOperation(notMemoryLock);
  }

  protected static FetchDataHigh Read_DataHigh(boolean notMemoryLock)
  {
    return new FetchDataHigh(notMemoryLock);
  }

  protected static FetchDataLow Read_DataLow(boolean notMemoryLock)
  {
    return new FetchDataLow(notMemoryLock);
  }

  public AddressingMode getAddressingMode()
  {
    return addressingMode;
  }

  public void executeOnFallingEdge(Cpu65816 cpu)
  {
    BusCycle busCycle = cycles.get(cpu.getCycle());
    Address address = busCycle.getAddress(cpu);
    cpu.setPinsAddress(address.getOffset());
    cpu.setPinsData(address.getBank());
  }

  public void executeOnRisingEdge(Cpu65816 cpu, OpCode opCode)
  {
    BusCycle busCycle = cycles.get(cpu.getCycle());
    List<Operation> operations = busCycle.getOperations();
    for (Operation operation : operations)
    {
      operation.execute(cpu, opCode);
    }
  }
}

