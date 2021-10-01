package name.bizna.emu65816.addressingmode;

import static name.bizna.emu65816.AddressingMode.DirectIndexedIndirectWithX;

public class DirectIndexedIndirectWithXCycles
    extends InstructionCycles
{
  //11
  public DirectIndexedIndirectWithXCycles(boolean read)
  {
    super(DirectIndexedIndirectWithX,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_D0(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), IO(), new NoteTwo()),
          new BusCycle(Address(PBR(), PC()), IO()),
          new BusCycle(Address(DP(), D0(), X()), Read_AAL()),
          new BusCycle(Address(DP(), D0(), X(), o(1)), Read_AAH()),
          new BusCycle(Address(DBR(), AA()), ExecuteLow_DoneIf8Bit(read)),
          new BusCycle(Address(DBR(), AA(), o(1)), ExecuteHigh_DoneIf16Bit(read)));
  }
}

