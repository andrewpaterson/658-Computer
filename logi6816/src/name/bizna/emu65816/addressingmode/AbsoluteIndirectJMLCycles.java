package name.bizna.emu65816.addressingmode;

import static name.bizna.emu65816.AddressingMode.*;

public class AbsoluteIndirectLongJMLCycles
    extends InstructionCycles
{
  public AbsoluteIndirectLongJMLCycles()
  {
    super(AbsoluteIndirectLong,
          new BusCycle(Address(PBR(), PC(), new FetchOpCode(), new IncrementProgramCounter()),
          new BusCycle(Address(PBR(), PC(), new Fetch_AAL(true), new IncrementProgramCounter()),
          new BusCycle(Address(PBR(), PC(), new Fetch_AAH(true)),
          new BusCycle(Address(AA(), new Fetch_New_PCL(true)),
          new BusCycle(Address(AA(), o(1), new Fetch_New_PCH(true)),
          new BusCycle(Address(AA(), o(2), new FetchNewProgramCounterBank(true), new SetProgramCounter(New_PC())));
  }
}

