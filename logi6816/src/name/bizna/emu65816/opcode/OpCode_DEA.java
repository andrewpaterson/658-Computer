package name.bizna.emu65816.opcode;

import name.bizna.emu65816.addressingmode.InstructionCycles;

public class OpCode_DEA
    extends OpCode
{
  public OpCode_DEA(int mCode, InstructionCycles busCycles)
  {
    super("DEC", "Decrement accumulator; update NZ.", mCode, busCycles);
  }
}

