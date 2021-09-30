package name.bizna.emu65816.addressingmode;

import static name.bizna.emu65816.AddressingMode.DirectIndexedWithX;

public class DirectIndexedWithXCycles
    extends InstructionCycles
{
  public DirectIndexedWithXCycles(boolean read)
  {
    super(DirectIndexedWithX,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_pp()),
          new BusCycle(Address(PBR(), PC()), Read_D0(), PC_pp()),
          new BusCycle(Address(PBR(), PC()), DPL_ne_0()),
          new BusCycle(Address(PBR(), PC()), IO()),
          new BusCycle(Address(DirectPage(), D0(), X(), new Execute1()),
          new BusCycle(Address(DirectPage(), D0(), X(), o(1), new Execute2(true, read)));
  }
}

