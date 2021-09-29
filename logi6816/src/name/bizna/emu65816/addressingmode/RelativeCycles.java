package name.bizna.emu65816.addressingmode;

import static name.bizna.emu65816.AddressingMode.Relative;

public class RelativeCycles
    extends InstructionCycles
{
  public RelativeCycles()
  {
    super(Relative,
          new BusCycle(new ProgramCounter(), new FetchOpCode(), new IncrementProgramCounter()),
          new BusCycle(new ProgramCounter(), new FetchRelativeOffsetLow()),
          new BusCycle(new ProgramCounter(), new ExecuteLow(true, true)));  //Skip if branch not taken.
  }
}

