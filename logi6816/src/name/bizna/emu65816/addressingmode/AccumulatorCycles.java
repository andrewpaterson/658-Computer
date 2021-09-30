package name.bizna.emu65816.addressingmode;

import name.bizna.emu65816.AddressingMode;

public class Accumulator
    extends InstructionCycles
{
  public Accumulator()
  {
    super(AddressingMode.Accumulator,
          new BusCycle(new ProgramCounter(), new FetchOpCode(), new IncrementProgramCounter()),
          new BusCycle(new ProgramCounter(), new ExecuteLow(true, true)));
  }
}

