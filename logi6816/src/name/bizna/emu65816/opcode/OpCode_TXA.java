package name.bizna.emu65816.opcode;

import name.bizna.emu65816.addressingmode.InstructionCycles;

public class OpCode_TXA
    extends OpCode
{
  public OpCode_TXA(int mCode, InstructionCycles cycles)
  {
    super("TXA", "Transfer Index X to Accumulator", mCode, cycles);
  }
}

