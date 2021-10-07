package name.bizna.cpu.opcode;

import name.bizna.cpu.addressingmode.InstructionCycles;

public class OpCode_ROL
    extends OpCode
{
  public OpCode_ROL(int mCode, InstructionCycles cycles)
  {
    super("ROL", "Rotate Memory One Bit Left.", mCode, cycles);
  }
}

