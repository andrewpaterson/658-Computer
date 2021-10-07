package name.bizna.cpu.opcode;

import name.bizna.cpu.addressingmode.InstructionCycles;

public class OpCode_PHB
    extends OpCode
{
  public OpCode_PHB(int mCode, InstructionCycles cycles)
  {
    super("PHB", "Push Data Bank Register on Stack", mCode, cycles);
  }
}

