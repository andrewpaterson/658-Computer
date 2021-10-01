package name.bizna.emu65816.addressingmode;

import static name.bizna.emu65816.AddressingMode.DirectIndirectLongIndexedWithY;

public class DirectIndirectLongIndexedWithYCycles
    extends InstructionCycles
{
  //14
  public DirectIndirectLongIndexedWithYCycles(boolean read)
  {
    super(DirectIndirectLongIndexedWithY,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_D0(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), IO(), new NoteTwo()),
          new BusCycle(Address(DP(), D0()), Read_AAL()),
          new BusCycle(Address(DP(), D0(), o(1)), Read_AAH()),
          new BusCycle(Address(DP(), D0(), o(2)), Read_AAB()),
          new BusCycle(Address(AAB(), AA(), Y()), ExecuteLow_DoneIf8Bit(read)),
          new BusCycle(Address(AAB(), AA(), Y(), o(1)), ExecuteHigh_DoneIf16Bit(read)));
  }
}

