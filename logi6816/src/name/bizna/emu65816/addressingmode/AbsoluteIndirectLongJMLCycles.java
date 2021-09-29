package name.bizna.emu65816.addressingmode;

import static name.bizna.emu65816.AddressingMode.*;

public class AbsoluteIndirectLongJMLCycles
    extends InstructionCycles
{
  public AbsoluteIndirectLongJMLCycles()
  {
    super(AbsoluteIndirectLong,
          new BusCycle(new ProgramCounter(), new FetchOpCode(), new IncrementProgramCounter()),
          new BusCycle(new ProgramCounter(), new FetchAbsoluteAddressLow(true), new IncrementProgramCounter()),
          new BusCycle(new ProgramCounter(), new FetchAbsoluteAddressHigh(true)),
          new BusCycle(new AbsoluteAddress(), new FetchNewProgramCounterLow(true)),
          new BusCycle(new AbsoluteAddress(), new Offset(1), new FetchNewProgramCounterHigh(true)),
          new BusCycle(new AbsoluteAddress(), new Offset(2), new FetchNewProgramCounterBank(true), new SetProgramCounter(new NewProgramCounter())));
  }
}

