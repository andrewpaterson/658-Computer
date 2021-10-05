package name.bizna.emu65816.addressingmode;

import static name.bizna.emu65816.AddressingMode.OpCode;

public class FetchOpCodeCycles
    extends InstructionCycles
{
  //22b
  public FetchOpCodeCycles()
  {
    super(OpCode,
          new BusCycle(Address(PBR(), PC()), OpCode(), PC_inc()));
  }
}

