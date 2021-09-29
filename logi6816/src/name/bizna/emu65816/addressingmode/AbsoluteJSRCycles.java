package name.bizna.emu65816.addressingmode;

import static name.bizna.emu65816.AddressingMode.Absolute;

public class AbsoluteJSRCycles
    extends InstructionCycles
{
  public AbsoluteJSRCycles(boolean read)
  {
    super(Absolute,
          new BusCycle(new ProgramCounter(), new FetchOpCode(), new IncrementProgramCounter()),
          new BusCycle(new ProgramCounter(), new FetchNewProgramCounterLow(true), new ClearNewProgramCounterBank(), new IncrementProgramCounter()),
          new BusCycle(new ProgramCounter(), new FetchNewProgramCounterHigh(true)),
          new BusCycle(new ProgramCounter(), new InternalOperation(true)),
          new BusCycle(new StackPointer(), new WriteProgramCounterHigh(), new DecrementStackPointer()),
          new BusCycle(new StackPointer(), new WriteProgramCounterLow(), new SetProgramCounter(new NewProgramCounter())));
  }
}

