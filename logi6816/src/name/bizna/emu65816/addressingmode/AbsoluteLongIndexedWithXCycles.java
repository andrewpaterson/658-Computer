package name.bizna.emu65816.addressingmode;

import static name.bizna.emu65816.AddressingMode.AbsoluteLongIndexedWithX;

public class AbsoluteLongIndexedWithXCycles
    extends InstructionCycles
{
  //5
  public AbsoluteLongIndexedWithXCycles(boolean read)
  {
    super(AbsoluteLongIndexedWithX,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_pp()),
          new BusCycle(Address(PBR(), PC()), Read_AAL(), PC_pp()),
          new BusCycle(Address(PBR(), PC()), Read_AAH(), PC_pp()),
          new BusCycle(Address(PBR(), PC()), Read_AAB(), PC_pp()),
          new BusCycle(Address(AAB(), AA(), X()), ExecuteLow(read, true)),
          new BusCycle(Address(AAB(), AA(), X(), o(1)), ExecuteHigh(read, true)));
  }
}

