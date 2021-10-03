package name.bizna.emu65816.addressingmode;

import static name.bizna.emu65816.AddressingMode.StackRelativeIndirectIndexedWithY;

public class StackRelativeIndirectIndexedWithYCycles
    extends InstructionCycles
{
  public StackRelativeIndirectIndexedWithYCycles(boolean read)
  {
    super(StackRelativeIndirectIndexedWithY,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_D0(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), IO()),
          new BusCycle(Address(S(), D0()), Read_AAL()),
          new BusCycle(Address(S(), D0(), o(1)), Read_AAH()),
          new BusCycle(Address(S(), D0(), o(1)), IO()),
          new BusCycle(Address(DBR(), AA(), Y()), ExecuteLow_DoneIf8Bit(read)),
          new BusCycle(Address(DBR(), AA(), Y(), o(1)), ExecuteHigh_DoneIf16Bit(read)));
  }
}

