package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.Width;
import name.bizna.emu65816.interrupt.InterruptVector;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

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

  protected static AddressOffset[] Address(AddressOffset... addressOffsets)
  {
    return addressOffsets;
  }

  protected static WriteDataHigh Write_DataHigh()
  {
    return Write_DataHigh(true);
  }

  protected static WriteDataHigh Write_DataHigh(boolean notMemoryLock)
  {
    return new WriteDataHigh(notMemoryLock);
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

  protected static InterruptAddress VA(InterruptVector interruptVector)
  {
    return new InterruptAddress(interruptVector);
  }

  protected static DirectOffset DirectOffset()
  {
    return new DirectOffset();
  }

  protected static DirectPage DP()
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

  protected static IncrementProgramCounter PC_inc()
  {
    return new IncrementProgramCounter();
  }

  protected static DecrementProgramCounter PC_dec()
  {
    return new DecrementProgramCounter();
  }

  protected static DecrementStackPointer SP_dec()
  {
    return new DecrementStackPointer();
  }

  protected static IncrementStackPointer SP_inc()
  {
    return new IncrementStackPointer();
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

  protected static FetchDirectOffset Read_D0()
  {
    return new FetchDirectOffset(true);
  }

  protected static FetchAbsoluteAddressBank Read_AAB()
  {
    return new FetchAbsoluteAddressBank(true);
  }

  protected static FetchDataBank Read_DBR()
  {
    return new FetchDataBank(true);
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

  protected static FetchDataHigh Read_DataHigh()
  {
    return Read_DataHigh(true);
  }

  protected static FetchDataHigh Read_DataHigh(boolean notMemoryLock)
  {
    return new FetchDataHigh(notMemoryLock);
  }

  protected static FetchDataLow Read_DataLow(boolean notMemoryLock)
  {
    return new FetchDataLow(notMemoryLock);
  }

  protected static FetchDataLow Read_DataLow()
  {
    return Read_DataLow(true);
  }

  protected static WriteDataLow Write_DataLow()
  {
    return Write_DataLow(true);
  }

  protected static FetchDirectOffset Read_D0(boolean notMemoryLock)
  {
    return new FetchDirectOffset(notMemoryLock);
  }

  protected static WriteDataLow Write_DataLow(boolean notMemoryLock)
  {
    return new WriteDataLow(notMemoryLock);
  }

  protected static DoneInstruction DONE()
  {
    return new DoneInstruction();
  }

  @SuppressWarnings("SameParameterValue")
  protected static SetProgramBank PBR_e(int bank)
  {
    return new SetProgramBank(bank);
  }

  public static WriteAbsoluteAddressLow Write_AAL()
  {
    return new WriteAbsoluteAddressLow();
  }

  public static WriteAbsoluteAddressHigh Write_AAH()
  {
    return new WriteAbsoluteAddressHigh();
  }

  protected static ExecuteIf16BitMemory E16Bit(Consumer<Cpu65816> consumer)
  {
    return new ExecuteIf16BitMemory(consumer);
  }

  protected static ExecuteIf8BitMemory E8Bit(Consumer<Cpu65816> consumer)
  {
    return new ExecuteIf8BitMemory(consumer);
  }

  protected static Execute E(Consumer<Cpu65816> consumer)
  {
    return new Execute(consumer);
  }

  protected static Operation DONE16Bit(Width width)
  {
    return new DoneInstructionIf16Bit(width);
  }

  protected static Operation DONE8Bit(Width width)
  {
    return new DoneInstructionIf8Bit(width);
  }

  public AddressingMode getAddressingMode()
  {
    return addressingMode;
  }

  public final void executeOnFallingEdge(Cpu65816 cpu)
  {
    cycles.get(cpu.getCycle()).executeOnFallingEdge(cpu);
  }

  public final void executeOnRisingEdge(Cpu65816 cpu)
  {
    cycles.get(cpu.getCycle()).executeOnRisingEdge(cpu);
  }
}
