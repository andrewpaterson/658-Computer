package name.bizna.emu65816.addressingmode;

import static name.bizna.emu65816.AddressingMode.DirectIndexedWithY;

public class DirectIndexedWithYCycles
    extends InstructionCycles
{
  public DirectIndexedWithYCycles(boolean read)
  {
    super(DirectIndexedWithY,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_pp()),
          new BusCycle(Address(PBR(), PC()), Read_D0(), PC_pp()),
          new BusCycle(Address(PBR(), PC()), DPL_ne_0()),
          new BusCycle(Address(PBR(), PC()), IO()),
          new BusCycle(Address(DirectPage(), D0(), Y(), new Execute1()),
          new BusCycle(Address(DirectPage(), D0(), Y(), o(1), new Execute2(true, read)));
  }
}

