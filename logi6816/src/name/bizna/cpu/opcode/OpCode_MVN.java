package name.bizna.cpu.opcode;

import name.bizna.cpu.addressingmode.InstructionCycles;

public class OpCode_MVN
    extends OpCode
{
  public OpCode_MVN(int mCode, InstructionCycles cycles)
  {
    super("MVN", "Block move next...", mCode, cycles);
  }
}

