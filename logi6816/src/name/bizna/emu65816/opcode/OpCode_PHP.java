package name.bizna.emu65816.opcode;

import name.bizna.emu65816.addressingmode.InstructionCycles;

public class OpCode_PHP
    extends OpCode
{
  public OpCode_PHP(int mCode, InstructionCycles cycles)
  {
    super("PHP", "Push Processor Status on Stack", mCode, cycles);
  }
}

