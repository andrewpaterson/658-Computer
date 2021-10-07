package name.bizna.cpu.opcode;

import name.bizna.cpu.addressingmode.InstructionCycles;

public class OpCode_PHD
    extends OpCode
{
  public OpCode_PHD(int mCode, InstructionCycles cycles)
  {
    super("PHD", "Push Direct Register on Stack", mCode, cycles);
  }
}

