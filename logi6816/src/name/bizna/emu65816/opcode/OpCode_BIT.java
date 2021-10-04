package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Address;
import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Binary;
import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.addressingmode.InstructionCycles;

import static name.bizna.emu65816.Binary.is8bitValueNegative;
import static name.bizna.emu65816.OpCodeName.*;

public class OpCode_BIT
    extends OpCode
{
  public OpCode_BIT(int mCode, InstructionCycles cycles)
  {
    super("BIT", "Bit Test", mCode, cycles);
  }
}

