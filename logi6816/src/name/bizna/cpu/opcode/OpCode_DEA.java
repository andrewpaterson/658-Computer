package name.bizna.cpu.opcode;

import name.bizna.cpu.addressingmode.InstructionCycles;

public class OpCode_DEA
    extends OpCode
{
  public OpCode_DEA(int mCode, InstructionCycles busCycles)
  {
    super("DEC", "Decrement accumulator; update NZ.", mCode, busCycles);
  }
}

