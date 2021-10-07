package name.bizna.cpu.opcode;

import name.bizna.cpu.addressingmode.InstructionCycles;

public class OpCode_DEC
    extends OpCode
{
  public OpCode_DEC(int mCode, InstructionCycles busCycles)
  {
    super("DEC", "Decrement memory; result in memory and update NZ.", mCode, busCycles);
  }
}

