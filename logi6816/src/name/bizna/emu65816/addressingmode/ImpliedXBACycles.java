package name.bizna.emu65816.addressingmode;

import static name.bizna.emu65816.AddressingMode.Implied;

public class ImpliedXBACycles
    extends InstructionCycles
{
  //19b
  public ImpliedXBACycles()
  {
    super(Implied,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), IO()),
          new BusCycle(Address(PBR(), PC()), IO(), new Execute1(), new Execute2(), DONE()));
  }
}

