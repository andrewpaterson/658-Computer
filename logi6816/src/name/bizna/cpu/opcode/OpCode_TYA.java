package name.bizna.cpu.opcode;

import name.bizna.cpu.addressingmode.InstructionCycles;

public class OpCode_TYA
    extends OpCode
{
  public OpCode_TYA(int mCode, InstructionCycles cycles)
  {
    super("TYA", "Transfer Index Y to Accumulator", mCode, cycles);
  }
}

