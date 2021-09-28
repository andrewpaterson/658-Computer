package name.bizna.emu65816.addressingmode;

import static name.bizna.emu65816.AddressingMode.DirectPageIndexedIndirectWithX;

public class DirectPageIndexedIndirectWithXCycles
    extends InstructionCycles
{
  public DirectPageIndexedIndirectWithXCycles(boolean read)
  {
    super(DirectPageIndexedIndirectWithX,
          new BusCycle(new ProgramCounter(), new FetchOpCode(), new IncrementProgramCounter()),
          new BusCycle(new ProgramCounter(), new FetchDirectOffset(true), new IncrementProgramCounter()),
          new BusCycle(new ProgramCounter(), new DirectPageLowZero(true)),
          new BusCycle(new ProgramCounter(), new InternalIgnored(true)),
          new BusCycle(new DirectPage(), new DirectOffset(), new XIndex(), new FetchAbsoluteAddressLow(true)),
          new BusCycle(new DirectPage(), new DirectOffset(), new XIndex(), new Offset(1), new FetchAbsoluteAddressHigh(true)),
          new BusCycle(new DataBank(), new AbsoluteOffset(), new ExecuteLow(true, read)),
          new BusCycle(new DataBank(), new AbsoluteOffset(), new Offset(1), new ExecuteLow(true, read)));
  }
}

