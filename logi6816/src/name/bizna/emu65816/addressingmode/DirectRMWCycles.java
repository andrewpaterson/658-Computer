package name.bizna.emu65816.addressingmode;

import static name.bizna.emu65816.AddressingMode.Direct;

public class DirectRMWCycles
    extends InstructionCycles
{
  public DirectRMWCycles()
  {
    super(Direct,
          new BusCycle(new ProgramCounter(), new FetchOpCode(), new IncrementProgramCounter()),
          new BusCycle(new ProgramCounter(), new FetchDirectOffset(false), new IncrementProgramCounter()),
          new BusCycle(new ProgramCounter(), new DirectPageLowZero(false)),
          new BusCycle(new DirectPage(), new DirectOffset(), new FetchDataLow(false)),
          new BusCycle(new DirectPage(), new DirectOffset(), new Offset(1), new FetchDataHigh(false)),
          new BusCycle(new DirectPage(), new DirectOffset(), new Offset(1), new InternalIgnored(false)),
          new BusCycle(new DirectPage(), new DirectOffset(), new Offset(1), new ExecuteHigh(false, false)),
          new BusCycle(new DirectPage(), new DirectOffset(), new ExecuteLow(false, false)));
  }
}

