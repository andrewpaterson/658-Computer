package name.bizna.emu65816.opcode;

import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Binary;
import name.bizna.emu65816.Cpu65816;

public class OpCode_TSX
    extends OpCode
{
  public OpCode_TSX(String mName, int mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  @Override
  public void executeOnFallingEdge(Cpu65816 cpu)
  {
    int stackPointer = cpu.getStackPointer();
    if (cpu.indexIs8BitWide())
    {
      int stackPointerLower8Bits = Binary.lower8BitsOf(stackPointer);
      cpu.setX(Binary.setLower8BitsOf16BitsValue(cpu.getX(), stackPointerLower8Bits));
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

