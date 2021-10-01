package name.bizna.emu65816.addressingmode;

import static name.bizna.emu65816.AddressingMode.AbsoluteLong;

public class AbsoluteLongCycles
    extends InstructionCycles
{
  //4a
  public AbsoluteLongCycles(boolean read)
  {
    super(AbsoluteLong,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_AAL(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_AAH(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), Read_AAB(), PC_inc()),
          new BusCycle(Address(AAB(), AA()), ExecuteLow_DoneIf8Bit(read)),
          new BusCycle(Address(AAB(), AA(), o(1)), ExecuteHigh_DoneIf16Bit(read)));
  }
}

