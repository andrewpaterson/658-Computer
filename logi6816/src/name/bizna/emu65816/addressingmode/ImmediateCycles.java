package name.bizna.emu65816.addressingmode;

import static name.bizna.emu65816.AddressingMode.DirectPage;

public class DirectPageCycles
    extends InstructionCycles
{
  public DirectPageCycles(boolean read)
  {
    super(DirectPage,
          new BusCycle(new PCRelativeAddress(0), new OpCodeData()),
          new BusCycle(new PCRelativeAddress(1), new ReadDirectOffset(true)),
          new BusCycle(new PCRelativeAddress(1), new DirectPageLowNotZero(true)),
          new BusCycle(new DirectPageAddress(0), new ExecuteLow(true, read)),
          new BusCycle(new DirectPageAddress(1), new ExecuteHigh(true, read)));
  }
}

