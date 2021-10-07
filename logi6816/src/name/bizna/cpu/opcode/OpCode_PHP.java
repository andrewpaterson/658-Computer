package name.bizna.cpu.opcode;

import name.bizna.cpu.addressingmode.InstructionCycles;

public class OpCode_PHP
    extends OpCode
{
  public OpCode_PHP(int mCode, InstructionCycles cycles)
  {
    super("PHP", "Push Processor Status on Stack", mCode, cycles);
  }
}

