package name.bizna.emu65816.addressingmode;

import static name.bizna.emu65816.AddressingMode.StackRelativeIndirectIndexedWithY;

public class StackRelativeIndirectIndexedWithYSTACycles
    extends InstructionCycles
{
  public StackRelativeIndirectIndexedWithYSTACycles()
  {
    super(StackRelativeIndirectIndexedWithY,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_D0(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), IO(), SP_inc()),
          new BusCycle(Address(S(), D0()), Read_AAL(), SP_inc()),
          new BusCycle(Address(S(), D0(), o(1)), Read_AAH()),
          new BusCycle(Address(S(), D0(), o(1)), IO()),
          new BusCycle(Address(DBR(), AA(), Y()), new Execute1(), Write_DataLow(), new DoneInstructionIf8BitMemory()),
          new BusCycle(Address(DBR(), AA(), Y(), o(1)), new Execute2(), Write_DataHigh(), new DoneInstructionIf16BitMemory()));
  }
}

