package name.bizna.emu65816.opcode;

import name.bizna.emu65816.addressingmode.InstructionCycles;

public class OpCode_INC
    extends OpCode
{
  public OpCode_INC(int mCode, InstructionCycles busCycles)
  {
    super("INC", "Increment memory; result in memory and update NZ.", mCode, busCycles);
  }
}

