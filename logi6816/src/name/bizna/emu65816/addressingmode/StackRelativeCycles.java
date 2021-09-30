package name.bizna.emu65816.addressingmode;

import static name.bizna.emu65816.AddressingMode.StackRelative;

public class StackRelativeCycles
    extends InstructionCycles
{
  public StackRelativeCycles(boolean read)
  {
    super(StackRelative,
          new BusCycle(Address(PBR(), PC()), Operation(OpCode(), PC_pp())),
          new BusCycle(Address(PBR(), PC()), Operation(new FetchStackPointerOffset(true), PC_pp())),
          new BusCycle(Address(PBR(), PC()), Operation(IO())),
          new BusCycle(Address(S()), StackOffset()), ExecuteLow(read, true)),
          new BusCycle(Address(S()), StackOffset(), o(1)), ExecuteHigh(read, true)));
  }
}

