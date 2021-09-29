package name.bizna.emu65816.addressingmode;

import static name.bizna.emu65816.AddressingMode.Absolute;

public class AbsoluteRMWCycles
    extends InstructionCycles
{
  public AbsoluteRMWCycles()
  {
    super(Absolute,
          new BusCycle(new ProgramCounter(), new FetchOpCode(), new IncrementProgramCounter()),
          new BusCycle(new ProgramCounter(), new FetchAbsoluteAddressLow(false), new IncrementProgramCounter()),
          new BusCycle(new DataBank(), new AbsoluteAddress(), new FetchDataLow(false)),
          new BusCycle(new DataBank(), new AbsoluteAddress(), new Offset(1), new FetchDataHigh(false)),
          new BusCycle(new DataBank(), new AbsoluteAddress(), new Offset(1), new InternalOperation(false)),
          new BusCycle(new DataBank(), new AbsoluteAddress(), new Offset(1), new ExecuteHigh(false, false)),
          new BusCycle(new DataBank(), new AbsoluteAddress(), new ExecuteLow(false, false)));
  }
}

