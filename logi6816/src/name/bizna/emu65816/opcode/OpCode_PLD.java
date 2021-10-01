package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.addressingmode.InstructionCycles;

public class OpCode_PLD
    extends OpCode
{
  public OpCode_PLD(int mCode, InstructionCycles cycles)
  {
    super("PLD", "Pull Direct Register from Stack", mCode, cycles);
  }
}

