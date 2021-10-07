package name.bizna.cpu.opcode;

import name.bizna.cpu.addressingmode.InstructionCycles;

public class OpCode_LDX
    extends OpCode
{
  public OpCode_LDX(int mCode, InstructionCycles cycles)
  {
    super("LDX", "Load Index X with Memory", mCode, cycles);
  }
}

