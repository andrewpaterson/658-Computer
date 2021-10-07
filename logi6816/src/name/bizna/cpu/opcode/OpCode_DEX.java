package name.bizna.cpu.opcode;

import name.bizna.cpu.addressingmode.InstructionCycles;

public class OpCode_DEX
    extends OpCode
{
  public OpCode_DEX(int mCode, InstructionCycles cycles)
  {
    super("DEX", "Decrement Index X by One.", mCode, cycles);
  }
}

