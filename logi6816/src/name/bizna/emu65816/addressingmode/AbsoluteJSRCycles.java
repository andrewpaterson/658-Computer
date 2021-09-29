package name.bizna.emu65816.addressingmode;

import static name.bizna.emu65816.AddressingMode.Absolute;

public class AbsoluteJMPCycles
    extends InstructionCycles
{
  public AbsoluteJMPCycles(boolean read)
  {
    super(Absolute,
          new BusCycle(new ProgramCounter(), new FetchOpCode(), new IncrementProgramCounter()),
          new BusCycle(new ProgramCounter(), new FetchAbsoluteAddressLow(true), new IncrementProgramCounter()),
          new BusCycle(new ProgramCounter(), new FetchAbsoluteAddressHigh(true), new SetProgramCounter(new AbsoluteAddress())));
  }
}

