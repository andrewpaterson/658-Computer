package name.bizna.emu65816.addressingmode;

import static name.bizna.emu65816.AddressingMode.DirectIndexedWithX;

public class DirectIndexedWithXRMWCycles
    extends InstructionCycles
{
  public DirectIndexedWithXRMWCycles()
  {
    super(DirectIndexedWithX,
          new BusCycle(new ProgramCounter(), new FetchOpCode(), new IncrementProgramCounter()),
          new BusCycle(new ProgramCounter(), new FetchDirectOffset(false), new IncrementProgramCounter()),
          new BusCycle(new ProgramCounter(), new DirectPageLowZero(false)),
          new BusCycle(new ProgramCounter(), new InternalIgnored(false)),
          new BusCycle(new DirectPage(), new DirectOffset(), new XIndex(), new FetchDataLow(false)),
          new BusCycle(new DirectPage(), new DirectOffset(), new XIndex(), new Offset(1), new FetchDataHigh(false)),
          new BusCycle(new DirectPage(), new DirectOffset(), new XIndex(), new Offset(1), new InternalIgnored(false)),
          new BusCycle(new DirectPage(), new DirectOffset(), new XIndex(), new Offset(1), new ExecuteHigh(false, false)),
          new BusCycle(new DirectPage(), new DirectOffset(), new XIndex(), new ExecuteLow(false, false)));
  }
}

