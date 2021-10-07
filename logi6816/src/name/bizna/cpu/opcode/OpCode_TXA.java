package name.bizna.cpu.opcode;

import name.bizna.cpu.addressingmode.InstructionCycles;

public class OpCode_TXA
    extends OpCode
{
  public OpCode_TXA(int mCode, InstructionCycles cycles)
  {
    super("TXA", "Transfer Index X to Accumulator", mCode, cycles);
  }
}

