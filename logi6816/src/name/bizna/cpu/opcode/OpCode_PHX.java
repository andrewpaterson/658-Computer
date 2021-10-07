package name.bizna.cpu.opcode;

import name.bizna.cpu.addressingmode.InstructionCycles;

public class OpCode_PHX
    extends OpCode
{
  public OpCode_PHX(int mCode, InstructionCycles cycles)
  {
    super("PHX", "Push Index X on Stack", mCode, cycles);
  }
}

