package name.bizna.emu65816.opcode;

import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Binary;
import name.bizna.emu65816.Cpu65816;

public class OpCode_TSX
    extends OpCode
{
  public OpCode_TSX(String mName, int mCode, InstructionCycles cycles)
  {
    super(mName, mCode, cycles);
  }

  @Override
  public void executeOnFallingEdge(Cpu65816 cpu)
  {
    int stackPointer = cpu.getStackPointer();
    if (cpu.isIndex8Bit())
    {
      int stackPointerLower8Bits = Binary.getLowByte(stackPointer);
      cpu.setX(Binary.setLowByte(cpu.getX(), stackPointerLower8Bits));
      cpu.getCpuStatus().updateSignAndZeroFlagFrom8BitValue(stackPointerLower8Bits);
    }
    else
    {
      cpu.setX(stackPointer);
      cpu.getCpuStatus().updateSignAndZeroFlagFrom16BitValue(cpu.getX());
    }
    cpu.addToProgramAddressAndCycles(1, 2);
  }

  @Override
  public void executeOnRisingEdge(Cpu65816 cpu)
  {
  }
}

