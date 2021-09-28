package name.bizna.emu65816.addressingmode;

import static name.bizna.emu65816.AddressingMode.DirectPageIndirect;

public class DirectPageIndirectCycles
    extends InstructionCycles
{
  public DirectPageIndirectCycles(boolean read)
  {
    super(DirectPageIndirect,
          new BusCycle(new ProgramCounter(), new FetchOpCode(), new IncrementProgramCounter()),
          new BusCycle(new ProgramCounter(), new FetchDirectOffset(true), new IncrementProgramCounter()),
          new BusCycle(new ProgramCounter(), new DirectPageLowZero(true)),
          new BusCycle(new DirectPage(), new DirectOffset(), new FetchAbsoluteAddressLow(true)),
          new BusCycle(new DirectPage(), new DirectOffset(), new Offset(1), new FetchAbsoluteAddressHigh(true)),
          new BusCycle(new DataBank(), new AbsoluteOffset(), new ExecuteLow(true, read)),
          new BusCycle(new DataBank(), new AbsoluteOffset(), new Offset(1), new ExecuteLow(true, read)));
  }
}

