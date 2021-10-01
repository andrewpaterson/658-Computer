package name.bizna.emu65816.addressingmode;

import static name.bizna.emu65816.AddressingMode.DirectIndexedWithY;

public class DirectIndexedWithYCycles
    extends InstructionCycles
{
  //17
  public DirectIndexedWithYCycles(boolean read)
  {
    super(DirectIndexedWithY,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_D0(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), IO(), new NoteTwo()),
          new BusCycle(Address(PBR(), PC()), IO()),
          new BusCycle(Address(DP(), D0(), Y()), ExecuteLow_DoneIf8Bit(read)),
          new BusCycle(Address(DP(), D0(), Y(), o(1)), ExecuteHigh_DoneIf16Bit(read)));
  }
}

