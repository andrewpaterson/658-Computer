package name.bizna.emu65816.addressingmode;

import static name.bizna.emu65816.AddressingMode.DirectIndexedIndirectWithX;

public class DirectIndexedIndirectWithXCycles
    extends InstructionCycles
{
  public DirectIndexedIndirectWithXCycles(boolean read)
  {
    super(DirectIndexedIndirectWithX,
          new BusCycle(new ProgramCounter(), new FetchOpCode(), new IncrementProgramCounter()),
          new BusCycle(new ProgramCounter(), new FetchDirectOffset(true), new IncrementProgramCounter()),
          new BusCycle(new ProgramCounter(), new DirectPageLowZero(true)),
          new BusCycle(new ProgramCounter(), new InternalOperation(true)),
          new BusCycle(new DirectPage(), new DirectOffset(), new XIndex(), new FetchAbsoluteAddressLow(true)),
          new BusCycle(new DirectPage(), new DirectOffset(), new XIndex(), new Offset(1), new FetchAbsoluteAddressHigh(true)),
          new BusCycle(new DataBank(), new AbsoluteAddress(), new ExecuteLow(true, read)),
          new BusCycle(new DataBank(), new AbsoluteAddress(), new Offset(1), new ExecuteLow(true, read)));
  }
}

