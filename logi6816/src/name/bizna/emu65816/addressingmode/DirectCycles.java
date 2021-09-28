package name.bizna.emu65816.addressingmode;

import static name.bizna.emu65816.AddressingMode.DirectPage;

public class DirectPageCycles
    extends InstructionCycles
{
  public DirectPageCycles(boolean read)
  {
    super(DirectPage,
          new BusCycle(new ProgramCounter(), new FetchOpCode(), new IncrementProgramCounter()),
          new BusCycle(new ProgramCounter(), new FetchDirectOffset(true), new IncrementProgramCounter()),
          new BusCycle(new ProgramCounter(), new DirectPageLowZero(true)),
          new BusCycle(new DirectPage(), new DirectOffset(), new ExecuteLow(true, read)),
          new BusCycle(new DirectPage(), new DirectOffset(), new Offset(1), new ExecuteHigh(true, read)));
  }
}

