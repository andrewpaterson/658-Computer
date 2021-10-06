package name.bizna.emu65816.opcode;

import name.bizna.emu65816.addressingmode.InstructionCycles;

public class OpCode_INA
    extends OpCode
{
  public OpCode_INA(int mCode, InstructionCycles busCycles)
  {
    super("INC", "Increment accumulator; update NZ.", mCode, busCycles);
  }
}

