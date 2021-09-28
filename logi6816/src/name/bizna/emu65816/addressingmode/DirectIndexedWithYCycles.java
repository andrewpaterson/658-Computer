package name.bizna.emu65816.addressingmode;

import static name.bizna.emu65816.AddressingMode.DirectIndexedWithX;
import static name.bizna.emu65816.AddressingMode.DirectIndexedWithY;

public class DirectIndexedWithYCycles
    extends InstructionCycles
{
  public DirectIndexedWithYCycles(boolean read)
  {
    super(DirectIndexedWithY,
          new BusCycle(new ProgramCounter(), new FetchOpCode(), new IncrementProgramCounter()),
          new BusCycle(new ProgramCounter(), new FetchDirectOffset(true), new IncrementProgramCounter()),
          new BusCycle(new ProgramCounter(), new DirectPageLowZero(true)),
          new BusCycle(new ProgramCounter(), new InternalIgnored(true)),
          new BusCycle(new DirectPage(), new DirectOffset(), new YIndex(), new ExecuteLow(true, read)),
          new BusCycle(new DirectPage(), new DirectOffset(), new YIndex(), new Offset(1), new ExecuteHigh(true, read)));
  }
}

