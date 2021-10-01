package name.bizna.emu65816.addressingmode;

import static name.bizna.emu65816.AddressingMode.WaitForInterrupt;

public class WaitForInterruptCycles
    extends InstructionCycles
{
  public WaitForInterruptCycles()
  {
    super(WaitForInterrupt,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), IO()),
          new BusCycle(Address(PBR(), PC()), IO(), new Execute1(), DONE()));
  }
}

