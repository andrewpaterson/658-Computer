package name.bizna.cpu.opcode;

import name.bizna.cpu.addressingmode.InstructionCycles;

public class OpCode_INA
    extends OpCode
{
  public OpCode_INA(int mCode, InstructionCycles busCycles)
  {
    super("INC", "Increment accumulator; update NZ.", mCode, busCycles);
  }
}

