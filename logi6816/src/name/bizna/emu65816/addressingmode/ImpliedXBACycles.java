package name.bizna.emu65816.addressingmode;

import static name.bizna.emu65816.AddressingMode.Immediate;

public class ImpliedXBACycles
    extends InstructionCycles
{
  public ImpliedXBACycles()
  {
    super(Immediate,
          new BusCycle(Address(PBR(), PC()), OpCode()),
          new BusCycle(Address(PBR(), PC()), new Execute1()),
          new BusCycle(Address(PBR(), PC()), IO()));
  }
}

