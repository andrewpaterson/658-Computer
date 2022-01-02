package net.integratedcircuits.wdc.wdc65816.instruction.address;

import net.integratedcircuits.wdc.wdc65816.Executor;
import net.integratedcircuits.wdc.wdc65816.W65C816;
import net.integratedcircuits.wdc.wdc65816.WidthFromRegister;
import net.integratedcircuits.wdc.wdc65816.instruction.AddressingMode;
import net.integratedcircuits.wdc.wdc65816.instruction.BusCycle;
import net.integratedcircuits.wdc.wdc65816.instruction.interrupt.InterruptVector;
import net.integratedcircuits.wdc.wdc65816.instruction.operations.*;
import net.util.EmulatorException;

import java.util.Arrays;
import java.util.List;

public class InstructionCycles
{
  protected static final boolean RMW = false;

  protected List<BusCycle> cycles;
  protected AddressingMode addressingMode;

  public InstructionCycles(AddressingMode addressingMode,
                           BusCycle... cycles)
  {
    this.addressingMode = addressingMode;
    this.cycles = Arrays.asList(cycles);
    for (int i = 0; i < cycles.length; i++)
    {
      BusCycle cycle = this.cycles.get(i);
      cycle.setCycle(i + 1);
    }

    this.validate();
  }

  private void validate()
  {
    validateDoneOperation();
  }

  protected void validateDoneOperation()
  {
    int done8 = 0;
    int done16 = 0;
    for (BusCycle cycle : cycles)
    {
      done8 += cycle.getDone8();
      done16 += cycle.getDone16();
    }

    if (done8 != 1 && done16 != 1)
    {
      throw new EmulatorException("Exactly [1] 8 bit and [1] 16 bit done  operation must be specified in an Instruction cycle.");
    }
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

  protected static ConstantOffset o(int offset)
  {
    return new ConstantOffset(offset);
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

  protected static InternalFirst OpCode()
  {
    return new InternalFirst();
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

  protected static ReadNewProgramCounterHigh Read_NewPCH()
  {
    return new ReadNewProgramCounterHigh(true);
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

  protected static ReadAbsoluteAddressHigh Read_AAH()
  {
    return new ReadAbsoluteAddressHigh(true, true);
  }

  protected static ReadAbsoluteAddressHigh Read_AAH(boolean notMemoryLock)
  {
    return new ReadAbsoluteAddressHigh(notMemoryLock, true);
  }

  protected static ReadNewProgramBank Read_NewPBR()
  {
    return new ReadNewProgramBank(true);
  }

  protected static ReadNewProgramCounterLow Read_NewPCL()
  {
    return new ReadNewProgramCounterLow(true);
  }

  protected static ReadAbsoluteAddressLow Read_AAL()
  {
    return new ReadAbsoluteAddressLow(true, true);
  }

  protected static ReadAbsoluteAddressLow Read_AAVL()
  {
    return new ReadAbsoluteAddressLow(true, false);
  }

  protected static ReadAbsoluteAddressHigh Read_AAVH()
  {
    return new ReadAbsoluteAddressHigh(true, false);
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

  protected static ReadDirectOffset Read_D0()
  {
    return new ReadDirectOffset(true);
  }

  protected static ReadAbsoluteAddressBank Read_AAB()
  {
    return new ReadAbsoluteAddressBank(true);
  }

  protected static ReadDataBank Read_DBR()
  {
    return new ReadDataBank(true);
  }

  protected static WriteProcessorStatus Write_PS()
  {
    return new WriteProcessorStatus();
  }

  protected static ReadProcessorStatus Read_PS()
  {
    return new ReadProcessorStatus();
  }

  protected static ReadAbsoluteAddressLow Read_AAL(boolean notMemoryLock)
  {
    return new ReadAbsoluteAddressLow(notMemoryLock, true);
  }

  protected static InternalOperation IO(boolean notMemoryLock)
  {
    return new InternalOperation(notMemoryLock);
  }

  protected static ReadDataHigh Read_DataHigh()
  {
    return Read_DataHigh(true);
  }

  protected static ReadDataHigh Read_DataHigh(boolean notMemoryLock)
  {
    return new ReadDataHigh(notMemoryLock);
  }

  protected static ReadDataLow Read_DataLow(boolean notMemoryLock)
  {
    return new ReadDataLow(notMemoryLock);
  }

  protected static ReadDataLow Read_DataLow()
  {
    return Read_DataLow(true);
  }

  protected static WriteDataLow Write_DataLow()
  {
    return Write_DataLow(true);
  }

  protected static ReadDirectOffset Read_D0(boolean notMemoryLock)
  {
    return new ReadDirectOffset(notMemoryLock);
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

  protected static ExecuteIf16Bit E16Bit(Executor<W65C816> consumer, WidthFromRegister width)
  {

    return new ExecuteIf16Bit(consumer, width);
  }

  protected static ExecuteIf8Bit E8Bit(Executor<W65C816> consumer, WidthFromRegister width)
  {
    return new ExecuteIf8Bit(consumer, width);
  }

  protected static Execute E(Executor<W65C816> consumer)
  {
    return new Execute(consumer);
  }

  protected static Operation DONE16Bit(WidthFromRegister width)
  {
    return new DoneInstructionIf16Bit(width);
  }

  protected static Operation DONE8Bit(WidthFromRegister width)
  {
    return new DoneInstructionIf8Bit(width);
  }

  public AddressingMode getAddressingMode()
  {
    return addressingMode;
  }

  public BusCycle getBusCycle(int index)
  {
    if ((index >= 0) && (index < cycles.size()))
    {
      return cycles.get(index);
    }
    return null;
  }

  public int size()
  {
    return cycles.size();
  }
}

