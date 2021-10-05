package name.bizna.emu65816.opcode;

import name.bizna.emu65816.addressingmode.InstructionCycles;

public class OpCode_NEXT
    extends OpCode
{
  public OpCode_NEXT(InstructionCycles cycles)
  {
    super("Fetch Opcode", "Fetch Opcode", -1, cycles);
  }
}

