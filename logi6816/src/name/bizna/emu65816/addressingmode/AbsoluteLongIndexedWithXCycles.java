package name.bizna.emu65816.addressingmode;

import static name.bizna.emu65816.AddressingMode.AbsoluteLongIndexedWithX;

public class AbsoluteLongIndexedWithXCycles
    extends InstructionCycles
{
  //5
  public AbsoluteLongIndexedWithXCycles(boolean read)
  {
    super(AbsoluteLongIndexedWithX,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_AAL(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_AAH(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_AAB(), PC_inc()),
          new BusCycle(Address(AAB(), AA(), X()), ExecuteLow_DoneIf8Bit(read)),
          new BusCycle(Address(AAB(), AA(), X(), o(1)), ExecuteHigh_DoneIf16Bit(read)));
  }
}

