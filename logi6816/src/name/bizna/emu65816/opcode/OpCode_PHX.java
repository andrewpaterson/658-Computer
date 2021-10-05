package name.bizna.emu65816.opcode;

import name.bizna.emu65816.addressingmode.InstructionCycles;

public class OpCode_PHX
    extends OpCode
{
  public OpCode_PHX(int mCode, InstructionCycles cycles)
  {
    super("PHX", "Push Index X on Stack", mCode, cycles);
  }
}

