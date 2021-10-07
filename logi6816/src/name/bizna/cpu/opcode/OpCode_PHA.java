package name.bizna.cpu.opcode;

import name.bizna.cpu.addressingmode.InstructionCycles;

public class OpCode_PHA
    extends OpCode
{
  public OpCode_PHA(int mCode, InstructionCycles cycles)
  {
    super("PHA", "Push Accumulator onto Stack", mCode, cycles);
  }
}

