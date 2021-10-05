package name.bizna.emu65816.opcode;

import name.bizna.emu65816.addressingmode.InstructionCycles;

public class OpCode_DEC_A
    extends OpCode
{
  public OpCode_DEC_A(int mCode, InstructionCycles busCycles)
  {
    super("DEC", "Decrement accumulator; update NZ.", mCode, busCycles);
  }
}

