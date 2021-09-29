package name.bizna.emu65816.addressingmode;

import static name.bizna.emu65816.AddressingMode.Immediate;
import static name.bizna.emu65816.AddressingMode.RelativeLong;

public class RelativeLongCycles
    extends InstructionCycles
{
  public RelativeLongCycles()
  {
    super(RelativeLong,
          new BusCycle(new ProgramCounter(), new FetchOpCode(), new IncrementProgramCounter()),
          new BusCycle(new ProgramCounter(), new FetchRelativeOffsetLow(), new IncrementProgramCounter()),
          new BusCycle(new ProgramCounter(), new FetchRelativeOffsetHigh()),
          new BusCycle(new ProgramCounter(), new ExecuteLow(true, true)));
  }
}

