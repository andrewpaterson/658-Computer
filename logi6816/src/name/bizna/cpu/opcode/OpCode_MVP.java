package name.bizna.cpu.opcode;

import name.bizna.cpu.addressingmode.InstructionCycles;

public class OpCode_MVP
    extends OpCode
{
  public OpCode_MVP(int mCode, InstructionCycles cycles)
  {
    super("MVP", "Block move previous...", mCode, cycles);
  }
}

