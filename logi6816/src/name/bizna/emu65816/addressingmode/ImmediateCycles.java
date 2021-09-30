package name.bizna.emu65816.addressingmode;

import static name.bizna.emu65816.AddressingMode.Immediate;

public class ImmediateCycles
    extends InstructionCycles
{
  public ImmediateCycles()
  {
    super(Immediate,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_pp()),
          new BusCycle(Address(PBR(), PC()), PC_pp(), new Execute1()),
          new BusCycle(Address(PBR(), PC()), new NoteEight(), new Execute2(true, true)));
  }
}

