package name.bizna.emu65816.addressingmode;

import static name.bizna.emu65816.AddressingMode.StackRelativeIndirectIndexedWithY;

public class StackRelativeIndirectIndexedWithYCycles
    extends InstructionCycles
{
  public StackRelativeIndirectIndexedWithYCycles(boolean read)
  {
    super(StackRelativeIndirectIndexedWithY,
          new BusCycle(Address(PBR(), PC()), Operation(OpCode(), PC_inc())),
          new BusCycle(Address(PBR(), PC()), Operation(new FetchStackPointerOffset(true))),
          new BusCycle(Address(PBR(), PC()), Operation(IO())),
          new BusCycle(Address(S()), StackOffset()), Operation(Read_AAL())),
          new BusCycle(Address(S()), StackOffset(), o(1)), Operation(Read_AAH())),
          new BusCycle(Address(S()), StackOffset(), o(1)), Operation(IO())),
          new BusCycle(Address(DBR(), AA(), Y()), ExecuteLow_DoneIf8Bit(read)),
          new BusCycle(Address(DBR(), AA(), Y(), o(1)), ExecuteHigh_DoneIf16Bit(read)));
  }
}

