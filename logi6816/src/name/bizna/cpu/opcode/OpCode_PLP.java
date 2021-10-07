package name.bizna.cpu.opcode;

import name.bizna.cpu.addressingmode.InstructionCycles;

public class OpCode_PLP
    extends OpCode
{
  public OpCode_PLP(int mCode, InstructionCycles cycles)
  {
    super("PLP", "Pull Processor Status from Stack", mCode, cycles);
  }
}

