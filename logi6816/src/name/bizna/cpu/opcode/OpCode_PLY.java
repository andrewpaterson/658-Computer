package name.bizna.cpu.opcode;

import name.bizna.cpu.addressingmode.InstructionCycles;

public class OpCode_PLY
    extends OpCode
{
  public OpCode_PLY(int mCode, InstructionCycles cycles)
  {
    super("PLY", "Pull Index Y from Stack", mCode, cycles);
  }
}

