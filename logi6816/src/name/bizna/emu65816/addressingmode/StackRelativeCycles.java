package name.bizna.emu65816.addressingmode;

import static name.bizna.emu65816.AddressingMode.StackRelative;

public class StackRelativeCycles
    extends InstructionCycles
{

  public StackRelativeCycles(boolean read)
  {
    super(StackRelative,
          new BusCycle(new ProgramCounter(), new FetchOpCode()),
          new BusCycle(new ProgramCounter(), new FetchStackPointerOffset(true)),
          new BusCycle(new ProgramCounter(), new InternalIgnored(true)),
          new BusCycle(new DirectPageAddress(0), new ExecuteLow(true, read)),
          new BusCycle(new DirectPageAddress(1), new ExecuteHigh(true, read)));
  }
}

