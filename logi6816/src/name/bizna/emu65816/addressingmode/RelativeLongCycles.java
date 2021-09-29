package name.bizna.emu65816.addressingmode;

import static name.bizna.emu65816.AddressingMode.Immediate;

public class RelativeCycles
    extends InstructionCycles
{
  public RelativeCycles()
  {
    super(Immediate,
          new BusCycle(new ProgramCounter(), new FetchOpCode(), new IncrementProgramCounter()),
          new BusCycle(new ProgramCounter(), new FetchRelativeOffset()),
          new BusCycle(new ProgramCounter(), new ExecuteLow(true, true)));  //Skip if branch not taken.
  }
}

