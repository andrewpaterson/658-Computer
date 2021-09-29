package name.bizna.emu65816.opcode;

import name.bizna.emu65816.AddressingMode;
import name.bizna.emu65816.Binary;
import name.bizna.emu65816.Cpu65816;

public class OpCode_TCS
    extends OpCode
{
  public OpCode_TCS(String mName, int mCode, AddressingMode mAddressingMode)
  {
    super(mName, mCode, mAddressingMode);
  }

  @Override
  public void executeOnFallingEdge(Cpu65816 cpu)
  {
    int stackPointer = cpu.getStackPointer();
    if (cpu.getCpuStatus().isEmulationMode())
    {
      stackPointer = Binary.setLower8BitsOf16BitsValue(stackPointer, Binary.getLowByte(cpu.getA()));
    }
    else
    {
      stackPointer = cpu.getA();
    }
    cpu.setStackPointer(stackPointer);
    cpu.addToProgramAddressAndCycles(1, 2);
  }

  @Override
  public void executeOnRisingEdge(Cpu65816 cpu)
  {
  }
}

