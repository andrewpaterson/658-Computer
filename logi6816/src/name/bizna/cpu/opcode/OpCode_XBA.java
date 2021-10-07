package name.bizna.cpu.opcode;

import name.bizna.cpu.addressingmode.InstructionCycles;

public class OpCode_XBA
    extends OpCode
{
  public OpCode_XBA(int mCode, InstructionCycles cycles)
  {
    super("XBA", "Exchange B and A Accumulator", mCode, cycles);
  }
}

