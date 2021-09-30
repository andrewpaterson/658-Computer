package name.bizna.emu65816.addressingmode;

import static name.bizna.emu65816.AddressingMode.DirectIndirectIndexedWithY;

public class DirectIndirectIndexedWithYCycles
    extends InstructionCycles
{
  public DirectIndirectIndexedWithYCycles(boolean read)
  {
    super(DirectIndirectIndexedWithY,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_pp()),
          new BusCycle(Address(PBR(), PC()), Read_D0(), PC_pp()),
          new BusCycle(Address(PBR(), PC()), DPL_ne_0()),
          new BusCycle(Address(DirectPage(), D0()), Read_AAL()),
          new BusCycle(Address(DirectPage(), D0(), o(1)), Read_AAH()),
          new BusCycle(Address(DBR(), AAH(), AAL_YL()), new NoteFour(true, read)),
          new BusCycle(Address(DBR(), AA(), Y()), new Execute1()),
          new BusCycle(Address(DBR(), AA(), Y(), o(1)), new Execute1()));
  }
}

