package name.bizna.emu65816.addressingmode;

import static name.bizna.emu65816.AddressingMode.DirectIndirect;

public class DirectIndirectCycles
    extends InstructionCycles
{
  public DirectIndirectCycles(boolean read)
  {
    super(DirectIndirect,
          new BusCycle(new ProgramCounter(), new FetchOpCode(), new IncrementProgramCounter()),
          new BusCycle(new ProgramCounter(), new FetchDirectOffset(true), new IncrementProgramCounter()),
          new BusCycle(new ProgramCounter(), new DirectPageLowZero(true)),
          new BusCycle(new DirectPage(), new DirectOffset(), new FetchAbsoluteAddressLow(true)),
          new BusCycle(new DirectPage(), new DirectOffset(), new Offset(1), new FetchAbsoluteAddressHigh(true)),
          new BusCycle(new DataBank(), new AbsoluteOffset(), new ExecuteLow(true, read)),
          new BusCycle(new DataBank(), new AbsoluteOffset(), new Offset(1), new ExecuteLow(true, read)));
  }
}

