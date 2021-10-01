package name.bizna.emu65816.addressingmode;

import static name.bizna.emu65816.AddressingMode.DirectIndexedWithX;

public class DirectIndexedWithXCycles
    extends InstructionCycles
{
  //16a
  public DirectIndexedWithXCycles(boolean read)
  {
    super(DirectIndexedWithX,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_D0(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), IO(), new NoteTwo()),
          new BusCycle(Address(PBR(), PC()), IO()),
          new BusCycle(Address(DP(), D0(), X()), ExecuteLow_DoneIf8Bit(read)),
          new BusCycle(Address(DP(), D0(), X(), o(1)), ExecuteHigh_DoneIf16Bit(read)));
  }
}

