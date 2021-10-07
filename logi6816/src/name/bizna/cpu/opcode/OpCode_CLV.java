package name.bizna.cpu.opcode;

import name.bizna.cpu.addressingmode.InstructionCycles;

public class OpCode_CLV
    extends OpCode
{
  public OpCode_CLV(int mCode, InstructionCycles cycles)
  {
    super("CLV", "Clear Overflow Flag", mCode, cycles);
  }
}

