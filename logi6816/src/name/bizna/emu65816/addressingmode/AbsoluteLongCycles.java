package name.bizna.emu65816.addressingmode;

import static name.bizna.emu65816.AddressingMode.AbsoluteLong;

public class AbsoluteLongCycles
    extends InstructionCycles
{
  //4a
  public AbsoluteLongCycles(boolean read)
  {
    super(AbsoluteLong,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_pp()),
          new BusCycle(Address(PBR(), PC()), Read_AAL(), PC_pp()),
          new BusCycle(Address(PBR(), PC()), Read_AAH(), PC_pp()),
          new BusCycle(Address(PBR(), PC()), Read_AAB(), PC_pp()),
          new BusCycle(Address(AAB(), AA()), ExecuteLow(read, true)),
          new BusCycle(Address(AAB(), AA(), o(1)), ExecuteHigh(read, true)));
  }
}

