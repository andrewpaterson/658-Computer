package name.bizna.emu65816.addressingmode;

import static name.bizna.emu65816.AddressingMode.StackRelative;

public class StackHardwareInterruptCycles
    extends InstructionCycles
{
  private int interruptVector;

  public StackHardwareInterruptCycles(boolean read, int interruptVector)
  {
    super(StackRelative,
          new BusCycle(new ProgramCounter(), new InternalOperation(true, true, true)),
          new BusCycle(new ProgramCounter(), new InternalOperation(true)),
          new BusCycle(new StackPointer(), new WriteProgramBank(),new DecrementStackPointer()),
          new BusCycle(new StackPointer(), new WriteProgramCounterHigh(),new DecrementStackPointer()),
          new BusCycle(new StackPointer(), new WriteProgramCounterLow(), new DecrementStackPointer()),
          new BusCycle(new StackPointer(), new WriteProcessorStatus(), new DecrementStackPointer()),
          new BusCycle(new InterruptVector(interruptVector), new FetchAbsoluteAddressLow(true)),
          new BusCycle(new InterruptVector(interruptVector), new Offset(1), new FetchAbsoluteAddressHigh(true), new SetProgramCounter(new AbsoluteAddress())));
  }
}

