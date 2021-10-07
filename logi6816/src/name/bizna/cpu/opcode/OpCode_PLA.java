package name.bizna.cpu.opcode;

import name.bizna.cpu.addressingmode.InstructionCycles;

public class OpCode_PLA
    extends OpCode
{
  public OpCode_PLA(int mCode, InstructionCycles cycles)
  {
    super("PLA", "Pull Accumulator from Stack", mCode, cycles);
  }
}

