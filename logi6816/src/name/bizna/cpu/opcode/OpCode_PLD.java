package name.bizna.cpu.opcode;

import name.bizna.cpu.addressingmode.InstructionCycles;

public class OpCode_PLD
    extends OpCode
{
  public OpCode_PLD(int mCode, InstructionCycles cycles)
  {
    super("PLD", "Pull Direct Register from Stack", mCode, cycles);
  }
}

