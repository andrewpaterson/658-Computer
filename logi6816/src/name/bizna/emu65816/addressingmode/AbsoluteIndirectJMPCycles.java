package name.bizna.emu65816.addressingmode;

import static name.bizna.emu65816.AddressingMode.AbsoluteIndirectLong;

public class AbsoluteIndirectJMPCycles
    extends InstructionCycles
{
  public AbsoluteIndirectJMPCycles()
  {
    super(AbsoluteIndirectLong,
          new BusCycle(new ProgramCounter(), new FetchOpCode(), new IncrementProgramCounter()),
          new BusCycle(new ProgramCounter(), new FetchAbsoluteAddressLow(true), new IncrementProgramCounter()),
          new BusCycle(new ProgramCounter(), new FetchAbsoluteAddressHigh(true)),
          new BusCycle(new AbsoluteAddress(), new FetchNewProgramCounterLow(true), new ClearNewProgramCounterBank()),
          new BusCycle(new AbsoluteAddress(), new Offset(1), new FetchNewProgramCounterHigh(true), new SetProgramCounter(new NewProgramCounter())));
  }
}

