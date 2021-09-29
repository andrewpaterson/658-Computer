package name.bizna.emu65816.addressingmode;

import static name.bizna.emu65816.AddressingMode.DirectIndirectIndexedWithY;

public class StackRelativeIndirectIndexedWithYCycles
    extends InstructionCycles
{
  public StackRelativeIndirectIndexedWithYCycles(boolean read)
  {
    super(DirectIndirectIndexedWithY,
          new BusCycle(new ProgramCounter(), new FetchOpCode(), new IncrementProgramCounter()),
          new BusCycle(new ProgramCounter(), new FetchStackPointerOffset(true)),
          new BusCycle(new ProgramCounter(), new InternalOperation(true)),
          new BusCycle(new StackPointer(), new StackOffset(), new FetchAbsoluteAddressLow(true)),
          new BusCycle(new StackPointer(), new StackOffset(), new Offset(1), new FetchAbsoluteAddressHigh(true)),
          new BusCycle(new StackPointer(), new StackOffset(), new Offset(1), new InternalOperation(true)),
          new BusCycle(new DataBank(), new AbsoluteAddress(), new YIndex(), new ExecuteLow(true, read)),
          new BusCycle(new DataBank(), new AbsoluteAddress(), new YIndex(), new Offset(1), new ExecuteHigh(true, read)));
  }
}

