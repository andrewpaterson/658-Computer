package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.addressingmode.InstructionCycles;

import static name.bizna.emu65816.OpCodeName.*;

public class OpCode_ASL
    extends OpCode
{
  public OpCode_ASL(int mCode, InstructionCycles busCycles)
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
        cpu.ASL();
        break;
      }
      default:
        throw new IllegalStateException("Unexpected value: " + getCode());
    }
  }
}

