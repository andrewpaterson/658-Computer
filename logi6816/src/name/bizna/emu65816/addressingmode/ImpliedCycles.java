package name.bizna.emu65816.addressingmode;

import static name.bizna.emu65816.AddressingMode.Immediate;

public class ImpliedCycles
    extends InstructionCycles
{
  public ImpliedCycles()
  {
    super(Immediate,
          new BusCycle(Address(PBR(), PC()), OpCode()),
          new BusCycle(Address(PBR(), PC()), new Execute1()));
  }
}

