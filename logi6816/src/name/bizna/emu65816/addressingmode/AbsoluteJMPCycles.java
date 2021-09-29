package name.bizna.emu65816.addressingmode;

import static name.bizna.emu65816.AddressingMode.Absolute;

public class AbsoluteJMPCycles
    extends InstructionCycles
{
  public AbsoluteJMPCycles(boolean read)
  {
    super(Absolute,
          new BusCycle(new ProgramCounter(), new FetchOpCode(), new IncrementProgramCounter()),
          new BusCycle(new ProgramCounter(), new FetchNewProgramCounterLow(true), new ClearNewProgramCounterBank(), new IncrementProgramCounter()),
          new BusCycle(new ProgramCounter(), new FetchNewProgramCounterHigh(true), new SetProgramCounter(new NewProgramCounter())));
  }
}

