package name.bizna.emu65816.addressingmode;

import static name.bizna.emu65816.AddressingMode.Implied;

public class ImpliedCycles
    extends InstructionCycles
{
  //19a
  public ImpliedCycles()
  {
    super(Implied,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()),
          new BusCycle(Address(PBR(), PC()), IO(), new Execute1(), new Execute2(), DONE()));
  }
}

