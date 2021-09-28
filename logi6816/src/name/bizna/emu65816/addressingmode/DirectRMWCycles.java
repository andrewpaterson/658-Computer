package name.bizna.emu65816.addressingmode;

import static name.bizna.emu65816.AddressingMode.Direct;

public class DirectCycles
    extends InstructionCycles
{
  public DirectCycles(boolean read)
  {
    super(Direct,
          new BusCycle(new ProgramCounter(), new FetchOpCode(), new IncrementProgramCounter()),
          new BusCycle(new ProgramCounter(), new FetchDirectOffset(true), new IncrementProgramCounter()),
          new BusCycle(new ProgramCounter(), new DirectPageLowZero(true)),
          new BusCycle(new DirectPage(), new DirectOffset(), new ExecuteLow(true, read)),
          new BusCycle(new DirectPage(), new DirectOffset(), new Offset(1), new ExecuteHigh(true, read)));
  }
}

