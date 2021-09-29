package name.bizna.emu65816.addressingmode;

import static name.bizna.emu65816.AddressingMode.Immediate;

public class ImmediateCycles
    extends InstructionCycles
{
  public ImmediateCycles()
  {
    super(Immediate,
          new BusCycle(new ProgramCounter(), new FetchOpCode(), new IncrementProgramCounter()),
          new BusCycle(new ProgramCounter(), new IncrementProgramCounter(), new ExecuteLow(true, true)),
          new BusCycle(new ProgramCounter(), new NoteEight(), new ExecuteHigh(true, true)));
  }
}

