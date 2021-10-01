package name.bizna.emu65816.addressingmode;

import static name.bizna.emu65816.AddressingMode.DirectIndirectIndexedWithY;

public class DirectIndirectIndexedWithYCycles
    extends InstructionCycles
{
  //13
  public DirectIndirectIndexedWithYCycles(boolean read)
  {
    super(DirectIndirectIndexedWithY,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_D0(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), IO(), new NoteTwo()),
          new BusCycle(Address(DP(), D0()), Read_AAL()),
          new BusCycle(Address(DP(), D0(), o(1)), Read_AAH()),
          new BusCycle(Address(DBR(), AAH(), AAL_YL()), new NoteFourY(read)),
          new BusCycle(Address(DBR(), AA(), Y()), ExecuteLow_DoneIf8Bit(read)),
          new BusCycle(Address(DBR(), AA(), Y(), o(1)), ExecuteHigh_DoneIf16Bit(read)));
  }
}

