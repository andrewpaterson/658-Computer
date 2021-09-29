package name.bizna.emu65816.addressingmode;

import static name.bizna.emu65816.AddressingMode.AbsoluteIndexedIndirectWithX;

public class AbsoluteIndexedIndirectWithXJSRCycles
    extends InstructionCycles
{
  public AbsoluteIndexedIndirectWithXJSRCycles()
  {
    super(AbsoluteIndexedIndirectWithX,
          new BusCycle(new ProgramCounter(), new FetchOpCode(), new IncrementProgramCounter()),
          new BusCycle(new ProgramCounter(), new FetchAbsoluteAddressLow(true), new IncrementProgramCounter()),
          new BusCycle(new StackPointer(), new WriteProgramCounterHigh(), new DecrementStackPointer()),
          new BusCycle(new StackPointer(), new WriteProgramCounterLow(), new DecrementStackPointer()),
          new BusCycle(new ProgramCounter(), new FetchAbsoluteAddressHigh(true)),
          new BusCycle(new ProgramCounter(), new InternalOperation(true)),
          new BusCycle(new ProgramBank(), new AbsoluteAddress(), new XIndex(), new FetchNewProgramCounterLow(true), new ClearNewProgramCounterBank()),
          new BusCycle(new ProgramBank(), new AbsoluteAddress(), new XIndex(), new Offset(1), new FetchNewProgramCounterHigh(true), new SetProgramCounter(new NewProgramCounter())));
  }
}

