package name.bizna.emu65816.opcode;

import name.bizna.emu65816.Binary;
import name.bizna.emu65816.Cpu65816;
import name.bizna.emu65816.addressingmode.InstructionCycles;

public class OpCode_TSX
    extends OpCode
{
  public OpCode_TSX(int mCode, InstructionCycles cycles)
  {
    super("TSX", "Transfer Stack Pointer Register to Index X", mCode, cycles);
  }

  @Override
  public void executeOnFallingEdge(Cpu65816 cpu)
  {
    int stackPointer = cpu.getStackPointer();
    if (cpu.isIndex8Bit())
    {
      int stackPointerLower8Bits = Binary.getLowByte(stackPointer);
      cpu.setX(Binary.setLowByte(cpu.getX(), stackPointerLower8Bits));
      cpu.getCpuStatus().setSignAndZeroFlagFrom8BitValue(stackPointerLower8Bits);
    }
    else
    {
      cpu.setX(stackPointer);
      cpu.getCpuStatus().setSignAndZeroFlagFrom16BitValue(cpu.getX());
    }
    cpu.addToProgramAddressAndCycles(1, 2);
  }
}

