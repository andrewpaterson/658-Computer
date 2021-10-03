package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Address;
import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Binary;
import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.addressingmode.InstructionCycles;

import static name.bizna.emu65816.OpCodeName.*;

public class OpCode_LDY
    extends OpCode
{
  public OpCode_LDY(int mCode, InstructionCycles cycles)
  {
    super("LDY", "Load Index Y with Memory",  mCode, cycles);
  }

  @Override
  public void execute1(Cpu65816 cpu)
  {
    if (cpu.isIndex8Bit())
    {
      cpu.setY(cpu.getData());
    }
  }

  @Override
  public void execute2(Cpu65816 cpu)
  {
    if (cpu.isIndex16Bit())
    {
      cpu.setY(cpu.getData());
    }
  }
}

