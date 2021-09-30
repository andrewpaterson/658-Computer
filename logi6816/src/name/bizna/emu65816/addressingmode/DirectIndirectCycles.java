package name.bizna.emu65816.addressingmode;

import static name.bizna.emu65816.AddressingMode.DirectIndirect;

public class DirectIndirectCycles
    extends InstructionCycles
{
  public DirectIndirectCycles(boolean read)
  {
    super(DirectIndirect,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_pp()),
          new BusCycle(Address(PBR(), PC()), Read_D0(), PC_pp()),
          new BusCycle(Address(PBR(), PC()), DPL_ne_0()),
          new BusCycle(Address(DirectPage(), D0(), Read_AAL()),
          new BusCycle(Address(DirectPage(), D0(), o(1), Read_AAH()),
          new BusCycle(Address(DBR(), AA(), new Execute1()),
          new BusCycle(Address(DBR(), AA(), o(1), new Execute1()));
  }
}

