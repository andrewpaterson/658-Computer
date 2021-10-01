package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.addressingmode.InstructionCycles;

import static name.bizna.emu65816.Binary.is16bitValueNegative;
import static name.bizna.emu65816.Binary.is8bitValueNegative;
import static name.bizna.emu65816.OpCodeName.*;
import static name.bizna.emu65816.Unsigned.toByte;
import static name.bizna.emu65816.Unsigned.toShort;

public class OpCode_ASL_Memory
    extends OpCode
{
  public OpCode_ASL_Memory(int mCode, InstructionCycles busCycles)
  {
    super("ASL", "Shift memory left 1 bit; result in memory and update NZC.", mCode, busCycles);
  }

  @Override
  public void execute1(Cpu65816 cpu)
  {
    switch (getCode())
    {
      case ASL_Absolute:
      case ASL_DirectPage:
      case ASL_AbsoluteIndexedWithX:
      case ASL_DirectPageIndexedWithX:
      {
        cpu.shiftLeftData();
        break;
      }
      default:
        throw new IllegalStateException("Unexpected value: " + getCode());
    }
  }
}

