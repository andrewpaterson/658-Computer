package name.bizna.emu65816.addressingmode;

import static name.bizna.emu65816.AddressingMode.DirectIndirectIndexedWithY;

public class StackRelativeIndirectIndexedWithYCycles
    extends InstructionCycles
{
  public StackRelativeIndirectIndexedWithYCycles(boolean read)
  {
    super(DirectIndirectIndexedWithY,
          new BusCycle(new ProgramCounter(), new FetchOpCode()),
          new BusCycle(new ProgramCounter(), new FetchStackPointerOffset(true)),
          new BusCycle(new ProgramCounter(), new InternalIgnored(true)),
          new BusCycle(new StackPointerAddress(0), new FetchAbsoluteAddressLow(true)),
          new BusCycle(new DirectPageAddress(1), new FetchAbsoluteAddressHigh(true)),
          new BusCycle(new DirectPageAddressPlusYLow(), new InternalIgnored(true)),
          new BusCycle(new DirectPageAddressPlusY(0), new ExecuteLow(true, read)),
          new BusCycle(new DirectPageAddressPlusY(1), new ExecuteHigh(true, read)));
    xxx
  }
}
