package name.bizna.emu65816.addressingmode;

import static name.bizna.emu65816.AddressingMode.DirectIndirectLongIndexedWithY;

public class DirectIndirectLongIndexedWithYCycles
    extends InstructionCycles
{
  public DirectIndirectLongIndexedWithYCycles(boolean read)
  {
    super(DirectIndirectLongIndexedWithY,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_pp()),
          new BusCycle(Address(PBR(), PC()), Read_D0(), PC_pp()),
          new BusCycle(Address(PBR(), PC()), DPL_ne_0()),
          new BusCycle(Address(DirectPage(), D0(), Read_AAL()),
          new BusCycle(Address(DirectPage(), D0(), o(1), Read_AAH()),
          new BusCycle(Address(DirectPage(), D0(), o(2), Read_AAB()),
          new BusCycle(Address(AAB(), AA(), Y(), new Execute1()),
          new BusCycle(Address(AAB(), AA(), Y(), o(1), new Execute2(true, read)));
  }
}

