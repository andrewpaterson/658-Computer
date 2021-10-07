package name.bizna.cpu.opcode;

import name.bizna.cpu.addressingmode.InstructionCycles;

public class OpCode_PEA
    extends OpCode
{
  public OpCode_PEA(int mCode, InstructionCycles cycles)
  {
    super("PEA", "Push Absolute Address", mCode, cycles);
  }
}

