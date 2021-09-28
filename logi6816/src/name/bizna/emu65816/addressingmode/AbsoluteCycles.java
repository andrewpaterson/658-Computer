package name.bizna.emu65816.addressingmode;

import static name.bizna.emu65816.AddressingMode.Absolute;

public class AbsoluteCycles
    extends InstructionCycles
{
  public AbsoluteCycles(boolean read)
  {
    super(Absolute,
          new BusCycle(new ProgramCounter(), new FetchOpCode(), new IncrementProgramCounter()),
          new BusCycle(new ProgramCounter(), new FetchAbsoluteAddressLow(true), new IncrementProgramCounter()),
          new BusCycle(new ProgramCounter(), new FetchAbsoluteAddressHigh(true)),
          new BusCycle(new DataBank(), new AbsoluteOffset(), new ExecuteLow(read, true)),
          new BusCycle(new DataBank(), new AbsoluteOffset(), new Offset(1), new ExecuteHigh(read, true)));
  }
}

