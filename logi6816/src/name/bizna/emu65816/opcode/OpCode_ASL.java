package name.bizna.emu65816.opcode;

import name.bizna.emu65816.addressingmode.InstructionCycles;

public class OpCode_ASL
    extends OpCode
{
  public OpCode_ASL(int mCode, InstructionCycles busCycles)
  {
    super("ASL", "Shift memory left 1 bit; result in memory and update NZC.", mCode, busCycles);
  }
}

