package name.bizna.emu65816.addressingmode;

import static name.bizna.emu65816.AddressingMode.AbsoluteIndexedIndirectWithX;

public class AbsoluteIndexedIndirectWithXJMPCycles
    extends InstructionCycles
{
  public AbsoluteIndexedIndirectWithXJMPCycles(boolean read)
  {
    super(AbsoluteIndexedIndirectWithX,
          new BusCycle(new ProgramCounter(), new FetchOpCode(), new IncrementProgramCounter()),
          new BusCycle(new ProgramCounter(), new FetchNewProgramCounterLow(true), new ClearNewProgramCounterBank(), new IncrementProgramCounter()),
          new BusCycle(new ProgramCounter(), new FetchNewProgramCounterHigh(true)),
          new BusCycle(new ProgramCounter(), new InternalOperation(true)),
          new BusCycle(new ProgramBank(), new AbsoluteAddress(), new XIndex(), new FetchNewProgramCounterLow(true)),
          new BusCycle(new ProgramBank(), new AbsoluteAddress(), new XIndex(), new Offset(1), new FetchNewProgramCounterHigh(true), new SetProgramCounter(new NewProgramCounter())));
  }
}

