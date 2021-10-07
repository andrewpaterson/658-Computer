package name.bizna.cpu.opcode;

import name.bizna.cpu.addressingmode.InstructionCycles;

public class OpCode_ASL
    extends OpCode
{
  public OpCode_ASL(int mCode, InstructionCycles busCycles)
  {
    super("ASL", "Shift memory left 1 bit; result in memory and update NZC.", mCode, busCycles);
  }
}

