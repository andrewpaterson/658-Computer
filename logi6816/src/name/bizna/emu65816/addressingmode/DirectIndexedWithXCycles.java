package name.bizna.emu65816.addressingmode;

import static name.bizna.emu65816.AddressingMode.DirectIndexedWithX;

public class DirectIndexedWithXCycles
    extends InstructionCycles
{
  public DirectIndexedWithXCycles(boolean read)
  {
    super(DirectIndexedWithX,
          new BusCycle(new ProgramCounter(), new FetchOpCode(), new IncrementProgramCounter()),
          new BusCycle(new ProgramCounter(), new FetchDirectOffset(true), new IncrementProgramCounter()),
          new BusCycle(new ProgramCounter(), new DirectPageLowZero(true)),
          new BusCycle(new ProgramCounter(), new InternalOperation(true)),
          new BusCycle(new DirectPage(), new DirectOffset(), new XIndex(), new ExecuteLow(true, read)),
          new BusCycle(new DirectPage(), new DirectOffset(), new XIndex(), new Offset(1), new ExecuteHigh(true, read)));
  }
}

