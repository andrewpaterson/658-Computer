package name.bizna.emu65816.addressingmode;

import static name.bizna.emu65816.AddressingMode.AbsoluteLong;

public class AbsoluteLongJMPCycles
    extends InstructionCycles
{
  public AbsoluteLongJMPCycles(boolean read)
  {
    super(AbsoluteLong,
          new BusCycle(new ProgramCounter(), new FetchOpCode(), new IncrementProgramCounter()),
          new BusCycle(new ProgramCounter(), new FetchNewProgramCounterLow(true), new IncrementProgramCounter()),
          new BusCycle(new ProgramCounter(), new FetchNewProgramCounterHigh(true), new IncrementProgramCounter()),
          new BusCycle(new ProgramCounter(), new FetchNewProgramCounterBank(true), new SetProgramCounter(new NewProgramCounter())));
  }
}

