package name.bizna.emu65816.addressingmode;

import static name.bizna.emu65816.AddressingMode.StackInterruptSoftware;

public class WaitForInterruptCycles
    extends InstructionCycles
{
  public WaitForInterruptCycles()
  {
    super(StackInterruptSoftware,
          new BusCycle(Address(PBR(), PC()), OpCode()),
          new BusCycle(Address(PBR(), PC()), IO()),
          new BusCycle(Address(PBR(), PC()), IO(), new Execute1()));
  }
}

