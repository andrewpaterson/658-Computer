package name.bizna.emu65816.opcode;

import name.bizna.emu65816.addressingmode.InstructionCycles;

public class OpCode_PLX
    extends OpCode
{
  public OpCode_PLX(int mCode, InstructionCycles cycles)
  {
    super("PLX", "Pull Index X from Stack", mCode, cycles);
  }
}

