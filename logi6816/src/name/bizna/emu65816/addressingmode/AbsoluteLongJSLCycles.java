package name.bizna.emu65816.addressingmode;

import static name.bizna.emu65816.AddressingMode.AbsoluteLong;

public class AbsoluteJSLCycles
    extends InstructionCycles
{
  public AbsoluteJSLCycles(boolean read)
  {
    super(AbsoluteLong,
          new BusCycle(Address(PBR(), PC(), new FetchOpCode(), new IncrementProgramCounter()),
          new BusCycle(Address(PBR(), PC(), new Fetch_New_PCL(true), new IncrementProgramCounter(), new ClearNewProgramCounterBank(), new IncrementProgramCounter()),
          new BusCycle(Address(PBR(), PC(), new Fetch_New_PCH(true), new IncrementProgramCounter()),
          new BusCycle(Address(S(), new WriteProgramCounterBank()),
          new BusCycle(Address(S(), new InternalOperation(true), new DecrementStackPointer()),
          new BusCycle(Address(PBR(), PC(), new FetchNewProgramCounterBank(true), new IncrementProgramCounter()),
          new BusCycle(Address(S(), new WriteProgramCounterHigh(), new DecrementStackPointer()),
          new BusCycle(Address(S(), new WriteProgramCounterLow(), new DecrementStackPointer(), new SetProgramCounter(New_PC())));
  }
}

