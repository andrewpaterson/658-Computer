package name.bizna.emu65816.addressingmode;

import static name.bizna.emu65816.AddressingMode.DirectIndexedIndirectWithX;

public class DirectIndexedIndirectWithXCycles
    extends InstructionCycles
{
  public DirectIndexedIndirectWithXCycles(boolean read)
  {
    super(DirectIndexedIndirectWithX,
          new BusCycle(Address(PBR(), PC()), Operation(OpCode(), PC_pp())),
          new BusCycle(Address(PBR(), PC()), Operation(Read_D0(), PC_pp())),
          new BusCycle(Address(PBR(), PC()), Operation(DPL_ne_0())),
          new BusCycle(Address(PBR(), PC()), Operation(IO())),
          new BusCycle(Address(DirectPage(), D0(), X()), Operation(Read_AAL())),
          new BusCycle(Address(DirectPage(), D0(), X(), o(1)), Operation(Read_AAH())),
          new BusCycle(Address(DBR(), AA()), ExecuteLow(read, true)),
          new BusCycle(Address(DBR(), AA(), o(1)), ExecuteHigh(read, true)));
  }
}

