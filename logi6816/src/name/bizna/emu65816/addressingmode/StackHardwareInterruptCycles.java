package name.bizna.emu65816.addressingmode;

import static name.bizna.emu65816.AddressingMode.StackRelative;

public class StackHardwareIInterruptCycles
    extends InstructionCycles
{

  public StackHardwareIInterruptCycles(boolean read)
  {
    super(StackRelative,
          new BusCycle(new ProgramCounter(), new FetchOpCode()),
          new BusCycle(new ProgramCounter(), new InternalOperation(true)),
          new BusCycle(new StackPointer(), WriteProgramBank(), DecrementStackPointer()),
          new BusCycle(new StackPointer(), WriteProgramHigh(), DecrementStackPointer()),
          new BusCycle(new StackPointer(), WriteProgramLow(), DecrementStackPointer()),
          new BusCycle(new StackPointer(), WriteProcessorStatus(), DecrementStackPointer()),
          new BusCycle(new StackPointer(), new StackOffset(), new ExecuteLow(true, read)),
          new BusCycle(new StackPointer(), new StackOffset(), new Offset(1), new ExecuteHigh(true, read)));
  }
}

