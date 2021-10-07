package name.bizna.cpu.opcode;

import name.bizna.cpu.addressingmode.InstructionCycles;

public class OpCode_INC
    extends OpCode
{
  public OpCode_INC(int mCode, InstructionCycles busCycles)
  {
    super("INC", "Increment memory; result in memory and update NZ.", mCode, busCycles);
  }
}

